
#!/bin/bash
npm run dev &

# run object detection service
cd face_recognition
python3 main.py