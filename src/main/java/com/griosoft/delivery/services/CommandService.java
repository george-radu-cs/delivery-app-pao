package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Command;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;
import main.java.com.griosoft.delivery.repositories.CommandRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class CommandService {
    //    private final CommandCSV repository;
    private final CommandRepository repository;

    public CommandService() {
//        repository = CommandCSV.getInstance();
        this.repository = new CommandRepository();
    }

    public List<Command> getCommands() throws Exception {
        return repository.getCommands();
    }

    public Stream<Command> getCommandsStream() throws Exception {
        return repository.getCommands().stream();
    }

    public Command getCommandById(String id) throws Exception {
        return this.getCommandsStream().filter(command -> command.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Command> getCommandsByCustomerId(String customerId) throws Exception {
        return this.getCommandsStream().filter(command -> command.getCustomerId().equals(customerId));
    }

    public Stream<Command> getCommandsByLocalId(String localId) throws Exception {
        return this.getCommandsStream().filter(command -> command.getLocalId().equals(localId));
    }

    public Stream<Command> getActiveCommands() throws Exception {
        return this.getCommandsStream().filter(command -> command.getStatus().equals(CommandStatus.PENDING));
    }

    public void printActiveCommandsForLocal(String localId) throws Exception {
        this.getActiveCommands().filter(command -> command.getLocalId().equals(localId)).forEach(System.out::println);
    }

    public void updateCommandStatus(String commandId, CommandStatus status) throws Exception {
        Command command = this.getCommandsStream().filter(c -> c.getId().equals(commandId)).findFirst().orElse(null);
        Objects.requireNonNull(command).setStatus(status);
        if (status == CommandStatus.DELIVERED) {
            command.setDeliveredAt(OffsetDateTime.now(ZoneOffset.UTC));
        }
        this.updateCommand(command);
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getCommandById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addCommand(Command command) throws Exception {
        repository.create(command);
    }

    public void updateCommand(Command command) throws Exception {
        repository.update(command);
    }

    public void deleteCommand(Command command) throws Exception {
        repository.delete(command);
    }
}
