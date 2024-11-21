const express = require('express');
const multer = require('multer');
const fs = require('fs');
const path = require('path');
const processAudio = require('./audioHandler'); // Import the audio handler module

const app = express();
const upload = multer({ dest: 'uploads/' });


// Middleware to parse JSON bodies and increase the limit to handle large images
app.use(express.json({ limit: '50mb' }));

// Route to handle incoming object detection data
app.post('/log-detection', (req, res) => {
    const { label, confidence, image } = req.body;

    // Log the detected object and confidence level
    console.log(`Detected: ${label} with confidence: ${confidence.toFixed(2)}%`);

    // Decode the base64 image and save it as a file
    /*const imageBuffer = Buffer.from(image, 'base64');
    const imagePath = path.join(__dirname, `detection_${Date.now()}.jpg`);
    
    fs.writeFile(imagePath, imageBuffer, (err) => {
        if (err) {
            console.error('Error saving image:', err);
            return res.status(500).send('Failed to save image');
        }
        console.log(`Image saved at: ${imagePath}`);
        res.status(200).send('Detection logged and image saved.');
    });*/
});

app.get('/', (req, res) => {
  res.send('Server is running!');
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
    res.status(200).send({ text: transcription });
  });
});


app.listen(3000, '0.0.0.0', () => {
  console.log('Server is running on http://localhost:3000');
});