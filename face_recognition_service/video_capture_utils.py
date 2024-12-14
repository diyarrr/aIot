

import cv2


def start_video_capture(camera_index=0):
    video_capture = cv2.VideoCapture(camera_index)
    return video_capture

def release_video_capture(video_capture):
    video_capture.release()
    cv2.destroyAllWindows()
