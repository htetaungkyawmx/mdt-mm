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

wss.on('connection', (ws) => {
    console.log('Client connected');

    ws.on('message', (message) => {
        console.log('Received from client:', message);

        if (message === "End of CSV") {
            ws.send("File transfer completed.");
        } else {

            setTimeout(() => {
                ws.send(`Processed: ${message}`);
            }, 500);
        }
    });

    ws.on('close', () => {
        console.log('Client disconnected');
    });
});

const PORT = 8081;
server.listen(PORT, () => {
    console.log(`Client is listening on http://localhost:${PORT}`);
});
