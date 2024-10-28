package org.marine.mdtmm.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.List;

@ServerEndpoint("/socket")
public class CSVProcessingWebSocket {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        List<String> lines = readCsvFile(message);
        for (int i = 0; i < lines.size(); i++) {
            String logMessage = "Line " + (i + 1) + ", count " + (i + 1) + " processed successfully";
            sendMessage(logMessage);
        }
    }

    private void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {

    }

    private List<String> readCsvFile(String filePath) {
        return List.of("Sample line 1", "Sample line 2");
    }
}
