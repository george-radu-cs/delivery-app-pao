package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.Command;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class CommandRepository {
    public void create(Command command) {
        String sql = "insert into commands values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sql)) {
            statement.setString(1, command.getId());
            statement.setString(2, command.getCustomerId());
            statement.setString(3, command.getLocalId());
            Timestamp createdAt = null;
            if (command.getCreatedAt() != null) {
                createdAt = Timestamp.valueOf(command.getCreatedAt().atZoneSameInstant(ZoneOffset.UTC)
                                                     .toLocalDateTime());
            }
            statement.setTimestamp(4, createdAt);
            Timestamp deliveredAt = null;
            if (command.getDeliveredAt() != null) {
                deliveredAt = Timestamp.valueOf(command.getDeliveredAt().atZoneSameInstant(ZoneOffset.UTC)
                                                       .toLocalDateTime());
            }
            statement.setTimestamp(5, deliveredAt);
            statement.setString(6, command.getStatus().name());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Command> read() {
        List<Command> commands = new ArrayList<>();
        String sql = "select * from commands";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                Command command = new Command.Builder()
                        .id(result.getString("id"))
                        .customerId(result.getString("customerId"))
                        .localId(result.getString("localId"))
                        .createdAt(result.getTimestamp("createdAt").toLocalDateTime().atOffset(ZoneOffset.UTC))
                        .deliveredAt(result.getTimestamp("deliveredAt") != null
                                ? result.getTimestamp("deliveredAt").toLocalDateTime().atOffset(ZoneOffset.UTC)
                                : null)
                        .status(CommandStatus.valueOf(result.getString("status")))
                        .build();
                commands.add(command);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return commands;
    }
}