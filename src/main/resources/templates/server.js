const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const fs = require('fs');
const path = require('path');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'client.html'));
});

let receivedData = [];
const txtFilePath = path.join(__dirname, 'received_data.txt');

wss.on('connection', (ws) => {
    console.log('Client connected');

    ws.on('message', (message) => {
        message = message.toString();

        if (message === "End Of CSV") {
            fs.writeFile(txtFilePath, receivedData.join('\n'), (err) => {
                if (err) {
                    console.error('Error saving file:', err);
                    ws.send("Error saving file.");
                } else {
                    ws.send("File transfer completed.");
                    console.log('Data saved at:', txtFilePath);
                }
            });
        } else if (message.startsWith("ExpectedCount")) {
            const count = message.split(":")[1];
            ws.send(`Ready to receive ${count} rows.`);
        } else {
            receivedData.push(message);
            ws.send(`Processed: ${message}`);
            broadcastToClients(`Received: ${message}`);
        }
    });

    ws.on('close', () => {
        console.log('Client disconnected');
        receivedData = [];
    });
});

function broadcastToClients(message) {
    wss.clients.forEach(client => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(message);
        }
    });
}

const PORT = 8081;
server.listen(PORT, () => {
    console.log(`Server is listening on http://localhost:${PORT}`);
});
