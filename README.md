# IoT Server Project

This IoT project is designed to integrate multiple technologies for home automation and security. The system enables communication between a mobile application, an IoT server running on a Raspberry Pi, and a relay module to control devices and provide security alerts. The project uses audio processing, speech recognition, and face recognition to deliver an intelligent and efficient solution.

---

## Features

1. **Speech-to-Text Processing**:
   - Converts audio commands sent from the mobile app into actionable text using Python and JavaScript.
   - Recognizes commands like "Turn on the lamp" or "Turn off the lamp" to control connected devices.

2. **Face Recognition**:
   - Detects and identifies faces in real-time from a video feed.
   - Sends alerts and intruder images to the mobile app if an unrecognized face is detected.

3. **Device Control**:
   - Uses a relay module to execute ON/OFF commands based on audio transcription.

4. **Raspberry Pi Integration**:
   - The server runs on a Raspberry Pi, controlled via SSH for remote configuration and management.

---

## System Components

1. **Mobile Application**:
   - Sends audio files to the server.
   - Receives security alerts and intruder images.

2. **IoT Server**:
   - Processes audio files and performs speech-to-text conversion.
   - Detects faces and sends alerts for unrecognized individuals.
   - Routes commands to the relay module for device control.

3. **Relay Module**:
   - Executes ON/OFF commands to control devices like lamps.

---

## Installation and Setup

### Prerequisites
- **Hardware**:
  - Raspberry Pi with SSH access.
  - Relay module for device control.
  - Camera for face recognition.
- **Software**:
  - Node.js
  - Python 3
  - Required Python libraries: `pydub`, `speech_recognition`, `opencv-python`

