package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.Command;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CommandCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/commands.csv";
    private static final String DELIMITER = ",";
    private static CommandCSV instance = null;
    private final List<Command> commands = new ArrayList<>();

    private CommandCSV() {
    }

    public static CommandCSV getInstance() {
        if (instance == null) {
            instance = new CommandCSV();
        }
        return instance;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void create(Command command) {
        this.commands.add(command);
    }

    public void update(Command command) {
        commands.set(commands.indexOf(command), command);
    }

    public void delete(Command command) {
        commands.remove(command);
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var commandsEntriesStream = this.readFromCSV();
            commandsEntriesStream
                    .filter(commandEntry -> commandEntry.length == 6)
                    .forEachOrdered(commandEntry -> {
                        OffsetDateTime createdAt = null, deliveredAt = null;
                        try {
                            createdAt = OffsetDateTime.parse(commandEntry[3]);
                            if (!commandEntry[4].equals("null")) {
                                deliveredAt = OffsetDateTime.parse(commandEntry[4]);
                            }
                        } catch (Exception e) {
                            AuditService.getInstance().log(e.getMessage());
                        }
                        this.commands.add(new Command.Builder()
                                .id(commandEntry[0])
                                .customerId(commandEntry[1])
                                .localId(commandEntry[2])
                                .createdAt(createdAt)
                                .deliveredAt(deliveredAt)
                                .status(CommandStatus.getCommandStatus(commandEntry[5]))
                                .build());
                    });
        } catch (Exception e) {
            System.out.println("Error while loading commands from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading commands from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var command : this.commands) {
                writer.write(command.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving commands to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving commands to CSV" + e.getMessage());
        }
    }
}

