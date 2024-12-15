

import face_recognition


# encode the given images
def load_images(reference_images):

    img_encodings = {}
    for name, image_path in reference_images.items():
        image = face_recognition.load_image_file(image_path)
        face_encoding = face_recognition.face_encodings(image)[0]
        img_encodings[name] = face_encoding

    return img_encodings

# take the video capture image and compare with the database images
def recognize_faces(frame, reference_face_encodings):
    face_locations = face_recognition.face_locations(frame)
    face_encodings = face_recognition.face_encodings(frame, face_locations)

    recognized_faces = []
    for face_location, face_encoding in zip(face_locations, face_encodings):
        matches = face_recognition.compare_faces(list(reference_face_encodings.values()), face_encoding)
        name = 'Unknown'
        if True in matches:
            matched_index = matches.index(True)
            name = list(reference_face_encodings.keys())[matched_index]
        recognized_faces.append((face_location, name))
    return recognized_faces