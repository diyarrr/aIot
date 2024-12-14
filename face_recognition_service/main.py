
import cv2
import face_recognition_utils as fr_utils
import video_capture_utils as vc_utils
import requests
import base64

# Load reference images
reference_images = {
    'Diyar': 'images/diyar.jpg',
}

# Load reference face encodings
reference_face_encodings = fr_utils.load_images(reference_images)

# Initialize video capture
video_capture = vc_utils.start_video_capture()


def send_intruder_alert(frame, server_url):
    # Convert the frame (image) to JPEG format and encode it to base64
    _, buffer = cv2.imencode('.jpg', frame)
    image_base64 = base64.b64encode(buffer).decode('utf-8')

    try:
        response = requests.post(f'{server_url}/log-detection', data=image_base64, headers={'Content-Type': 'application/octet-stream'})
        if response.status_code == 200:
            print("Intruder alert sent successfully.")
        else:
            print(f"Failed to send intruder alert. Status code: {response.status_code}")
    except Exception as e:
        print(f"Error sending intruder alert: {e}")


# Process video frames for face recognition
while True:
    ret, frame = video_capture.read()
    if not ret:
        break

    # Get recognized faces in the current frame
    recognized_faces = fr_utils.recognize_faces(frame, reference_face_encodings)

    # Check if there is at least one person and all are unknown
    if recognized_faces:  # Ensure at least one face is detected
        intruder_detected = all(name == 'Unknown' for (_, _, _, _), name in recognized_faces)
    else:
        intruder_detected = False  # No faces detected, no intruder

    # Debug output
    print(f"Intruder Detected: {intruder_detected}")

    # Send alert if an intruder is detected
    if intruder_detected:
        send_intruder_alert(frame, "http://localhost:3000")

    # Draw rectangles and labels on the frame
    for (top, right, bottom, left), name in recognized_faces:
        cv2.rectangle(frame, (left, top), (right, bottom), (0, 255, 0), 2)
        font = cv2.FONT_HERSHEY_DUPLEX
        cv2.putText(frame, name, (left + 6, bottom - 6), font, 0.5, (255, 255, 255), 1)

    #cv2.imshow('Face Recognition', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release video capture and close windows
vc_utils.release_video_capture(video_capture)
