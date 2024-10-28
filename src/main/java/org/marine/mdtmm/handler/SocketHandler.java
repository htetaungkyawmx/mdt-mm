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
    private final String filePath = "received_data.csv";

    public SocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String receivedData = message.getPayload();
        System.out.println("Received CSV row: " + receivedData);
        saveDataToFile(receivedData);
        if ("End of CSV".equals(receivedData)) {
            session.sendMessage(new TextMessage("All rows received successfully"));
        } else {
            messagingTemplate.convertAndSend("/topic/updates", "Processed: " + receivedData);
            session.sendMessage(new TextMessage("Processed: " + receivedData));
        }
    }

    private void saveDataToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
