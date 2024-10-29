package org.marine.mdtmm.handler;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SocketHandler extends TextWebSocketHandler {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String FILE_PATH = "received_data.csv";

    public SocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String receivedData = message.getPayload();
        logAndSaveData(receivedData);

        if ("End of CSV".equals(receivedData)) {
            sendMessage(session, "All rows received successfully");
        } else {
            sendMessage(session, "Processed: " + receivedData);
        }
    }

    private void logAndSaveData(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
        messagingTemplate.convertAndSend("/topic/updates", message);
    }
}
