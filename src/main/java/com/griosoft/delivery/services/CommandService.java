package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Command;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;
import main.java.com.griosoft.delivery.repositories.csv.CommandCSV;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class CommandService {
    CommandCSV repository;

    public CommandService() {
        repository = CommandCSV.getInstance();
    }

    public List<Command> getCommands() {
        return repository.getCommands();
    }

    public Stream<Command> getCommandsStream() {
        return repository.getCommands().stream();
    }

    public Command getCommandById(String id) {
        return this.getCommandsStream().filter(command -> command.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Command> getCommandsByCustomerId(String customerId) {
        return this.getCommandsStream().filter(command -> command.getCustomerId().equals(customerId));
    }

    public Stream<Command> getCommandsByLocalId(String localId) {
        return this.getCommandsStream().filter(command -> command.getLocalId().equals(localId));
    }

    public Stream<Command> getActiveCommands() {
        return this.getCommandsStream().filter(command -> command.getStatus().equals(CommandStatus.PENDING));
    }

    public void printActiveCommandsForLocal(String localId) {
        this.getActiveCommands().filter(command -> command.getLocalId().equals(localId)).forEach(System.out::println);
    }

    public void updateCommandStatus(String commandId, CommandStatus status) {
        Command command = this.getCommandsStream().filter(c -> c.getId().equals(commandId)).findFirst().orElse(null);
        Objects.requireNonNull(command).setStatus(status);
        if (status == CommandStatus.DELIVERED) {
            command.setDeliveredAt(OffsetDateTime.now());
        }
        this.updateCommand(command);
    }

    public String getNewUUID() {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getCommandById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addComomand(Command command) {
        repository.create(command);
    }

    public void updateCommand(Command command) {
        repository.update(command);
    }

    public void deleteCommand(Command command) {
        repository.delete(command);
    }
}
