package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.Local;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LocalCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/locals.csv";
    private static final String DELIMITER = ",";
    private static LocalCSV instance = null;
    private List<Local> locals = new ArrayList<>();

    private LocalCSV() {
    }

    public static LocalCSV getInstance() {
        if (instance == null) {
            instance = new LocalCSV();
        }
        return instance;
    }

    public List<Local> getLocals() {
        return locals;
    }

    public void setLocals(List<Local> locals) {
        this.locals = locals;
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var localsEntriesStream = this.readFromCSV();
            localsEntriesStream
                    .filter(localEntry -> localEntry.length == 12)
                    .forEachOrdered(localEntry -> this.locals.add(new Local.Builder()
                            .id(localEntry[0])
                            .administratorId(localEntry[1])
                            .name(localEntry[2])
                            .description(localEntry[3])
                            .addressId(localEntry[4])
                            .phoneNumber(localEntry[5])
                            .email(localEntry[6])
                            .openHour(OffsetTime.parse(localEntry[7]))
                            .closeHour(OffsetTime.parse(localEntry[8]))
                            .type(localEntry[9])
                            .category(localEntry[10])
                            .status(localEntry[11])
                            .build()));
        } catch (Exception e) {
            System.out.println("Error while loading locals from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading locals from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var local : this.locals) {
                writer.write(local.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving locals to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving locals to CSV" + e.getMessage());
        }
    }
}
