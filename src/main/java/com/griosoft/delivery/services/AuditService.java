package main.java.com.griosoft.delivery.services;

import java.io.FileWriter;
import java.time.Instant;

public class AuditService {
    private static final AuditService instance = new AuditService();
    private FileWriter writer;

    private AuditService() {
        try {
            writer = new FileWriter("./src/main/resources/audit.log", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AuditService getInstance() {
        return instance;
    }

    public void log(String message) {
        try {
            String logMessage = String.format("%s: %s\n", Instant.now(), message);
            writer.write(logMessage);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
