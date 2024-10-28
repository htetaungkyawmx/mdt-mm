package org.marine.mdtmm.simulator;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CSVSimulator {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CSVSimulator(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void simulateCSVData() {
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String simulatedRow = "Row " + i + " content";
                    messagingTemplate.convertAndSend("/socket", simulatedRow);
                    System.out.println("Sent: " + simulatedRow);
                    TimeUnit.SECONDS.sleep(5);
                }
                messagingTemplate.convertAndSend("/socket", "End of CSV");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
