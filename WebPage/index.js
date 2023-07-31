const videoElement  = document.getElementById('videoElement');
const startButton   = document.getElementById('startButton');
const stopButton    = document.getElementById('stopButton');

const canvas = document.createElement('canvas');
const ctx = canvas.getContext('2d');

canvas.width = videoElement.width;
canvas.height = videoElement.height;

let mediaRecorder;
let recordedChunks = [];

let socket = new WebSocket('ws://localhost:8080/ws');

let play = true;

function captureFrame() {
    ctx.drawImage(videoElement, 0, 0, canvas.width, canvas.height);
    const imageData = canvas.toDataURL('image/jpeg', 0.8); // You can choose the format and quality here
  
    // Send the image data over WebSocket
    console.log(imageData);
    socket.send(imageData);
  
    // Schedule the next frame capture
    if (play)
        requestAnimationFrame(captureFrame);
}

setupCamera();

async function setupCamera() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: true });
        videoElement.srcObject = stream;
        mediaRecorder = new MediaRecorder(stream);
        
        // When the MediaRecorder has data available, save it to 'recordedChunks'
        mediaRecorder.ondataavailable = (event) => {
            if (event.data.size > 0) {
                recordedChunks.push(event.data);
            }
        };

        mediaRecorder.onstop = () => {
            // console.log(recordedChunks.length);
            const blob = new Blob(recordedChunks, {type: 'video/webm'});
            sendVideo(blob);
            recordedChunks = [];
        }

        // mediaRecorder.start();
    }
    catch (error) {
        console.error('Error accessing the camera:', error);
    }
}
 
// Start recording 
startButton.addEventListener('click', () => {
    // setupCamera();
    // mediaRecorder.start();
    captureFrame();
    startButton.disable = true;
    stopButton.disable = false;
})

// Stop recording 
stopButton.addEventListener('click', () => {
    // mediaRecorder.stop();
    play = false;
    startButton.disable = false;
    stopButton.disable = true;
})

// WebSocket setup and sending the video data
function sendVideo(videoBlob) {
    console.log(videoBlob);

    const socket = new WebSocket('ws://localhost:8080/ws');
    socket.onopen = () => {
        socket.send(videoBlob);
        socket.close();
    }

    socket.onmessage = (event) => {
      console.log('Server response:', event.data);
      // You can handle the server response here if needed
      socket.close();
    };
  
    socket.onerror = (error) => {
      console.error('WebSocket error:', error);
      socket.close();
    };
  
    socket.onclose = () => {
      console.log('WebSocket connection closed.');
    };

}
