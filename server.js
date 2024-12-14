const express = require('express');
const multer = require('multer');
const fs = require('fs');
const path = require('path');
const processAudio = require('./audioHandler');

const app = express();
const upload = multer({ dest: 'uploads/' });

//const mobileAppURL = //'http:///*MOBILE_PHONE_IP_ADRESS*/:8080/';
//const relayAppURL = //'http://RELAY_MODULE_IP_ADRESS:80/relay';

app.use(express.json()); // For JSON requests

app.get('/', (req, res) => {
  res.send('Server is running!');
});

// Route to handle raw payload (base64 image)
app.post('/log-detection', express.raw({ type: '*/*', limit: '10mb' }), async (req, res) => {
  try {
    const base64String = req.body.toString('utf-8'); // Convert Buffer to string

    const response = await fetch(mobileAppURL, {
      method: 'POST',
      body: base64String,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    console.log(`Alert sent to mobile app: ${response.status}`);
    res.status(200).send('Intruder alert logged and forwarded to mobile app.');
  } catch (error) {
    console.error('Error forwarding alert to mobile app:', error.message);
    res.status(500).send('Failed to forward alert to mobile app.');
  }
});

// upload the audio file to the server and convert it to the text
app.post('/upload-audio', upload.single('audioFile'), (req, res) => {
  if (!req.file) {
    return res.status(400).send('No audio file uploaded');
  }

  console.log('Received audio file:', req.file.path);

  // Use the processAudio function from the imported module
  processAudio(req.file.path, (error, transcription) => {
    if (error) {
      return res.status(500).send('Error processing audio');
    }

    const lowerTranscription = transcription.toLowerCase(); // To make the comparison case-insensitive

    if ((lowerTranscription.includes("turn") && lowerTranscription.includes("on")) ||
        (lowerTranscription.includes("turn") && lowerTranscription.includes("light") && lowerTranscription.includes("on"))) {
        // Send a request to the URL with state "ON"
        fetch(relayAppURL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ relay: 1, state: "ON" })
        })
            .then(response => {
                if (response.ok) {
                    console.log("State ON request sent successfully");
                } else {
                    console.error("Error sending state ON request:", response.statusText);
                }
            })
            .catch(error => {
                console.error("Error sending state ON request:", error);
            });
    } else if ((lowerTranscription.includes("turn") && lowerTranscription.includes("off")) ||
              (lowerTranscription.includes("turn") && lowerTranscription.includes("light") && lowerTranscription.includes("off"))) {
        // Send a request to the URL with state "OFF"
        fetch(relayAppURL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ relay: 1, state: "OFF" })
        })
            .then(response => {
                if (response.ok) {
                    console.log("State OFF request sent successfully");
                } else {
                    console.error("Error sending state OFF request:", response.statusText);
                }
            })
            .catch(error => {
                console.error("Error sending state OFF request:", error);
            });
    }
  });
});


app.listen(3000, '0.0.0.0', () => {
  console.log('Server is running on http://localhost:3000');
});