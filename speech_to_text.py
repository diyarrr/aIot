import sys
from pydub import AudioSegment
import speech_recognition as sr
import os

# convert file format to .wav
def convert_to_wav(file_path):
    audio = AudioSegment.from_file(file_path)
    wav_path = "converted.wav"
    audio.export(wav_path, format="wav")
    return wav_path

# convert speech to text
def transcribe_audio(file_path):
    recognizer = sr.Recognizer()
    with sr.AudioFile(file_path) as source:
        audio_data = recognizer.record(source)

    try:
        text = recognizer.recognize_google(audio_data)
        print(text)  # Print to stdout for Node.js
    except sr.UnknownValueError:
        print("Error: Could not understand the audio")
    except sr.RequestError:
        print("Error: Could not request results from the speech recognition service")
    finally:
        # Clean up the converted file
        if os.path.exists(file_path):
            os.remove(file_path)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Error: No file path provided")
        sys.exit(1)

    input_file_path = sys.argv[1]
    converted_file_path = convert_to_wav(input_file_path)
    transcribe_audio(converted_file_path)
