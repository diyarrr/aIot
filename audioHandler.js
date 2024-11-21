const { spawn } = require('child_process');

function processAudio(filePath, callback) {
  console.log('Processing audio file:', filePath);

  // Spawn a child process to run the Python script
  const pythonProcess = spawn('python3', ['speech_to_text.py', filePath]);

  let transcription = '';

  // Capture standard output from the Python process
  pythonProcess.stdout.on('data', (data) => {
    transcription += data.toString();
  });

  // Capture standard error output
  pythonProcess.stderr.on('data', (data) => {
    console.error('Python error:', data.toString());
  });

  // Handle process exit
  pythonProcess.on('close', (code) => {
    if (code !== 0) {
      callback(new Error('Error processing audio'));
    } else {
      console.log('Transcription:', transcription.trim());
      callback(null, transcription.trim());
    }
  });
}

module.exports = processAudio;
