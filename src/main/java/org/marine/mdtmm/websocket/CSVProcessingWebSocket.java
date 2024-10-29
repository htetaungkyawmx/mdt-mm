package org.marine.mdtmm.websocket;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
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
        List<String> lines = List.of("Sample line 1", "Sample line 2");
        for (int i = 0; i < lines.size(); i++) {
            sendMessage("Line " + (i + 1) + ", processed successfully");
        }
    }

    private void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
