package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.Local;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class LocalRepository {
    public List<Local> getLocals() throws Exception {
        List<Local> locals = new ArrayList<>();
        String sqlQuery = "SELECT * FROM locals";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                Local local = new Local.Builder()
                        .id(result.getString("id"))
                        .administratorId(result.getString("administratorId"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .addressId(result.getString("addressId"))
                        .phoneNumber(result.getString("phoneNumber"))
                        .email(result.getString("email"))
                        .openHour(result.getTime("openHour").toLocalTime().atOffset(ZoneOffset.UTC))
                        .closeHour(result.getTime("closeHour").toLocalTime().atOffset(ZoneOffset.UTC))
                        .type(result.getString("type"))
                        .category(result.getString("category"))
                        .status(result.getString("status"))
                        .build();
                locals.add(local);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting locals");
        }
        return locals;
    }

    public void create(Local local) throws Exception {
        String sqlQuery = "INSERT INTO locals VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, local.getId());
            statement.setString(2, local.getAdministratorId());
            statement.setString(3, local.getName());
            statement.setString(4, local.getDescription());
            statement.setString(5, local.getAddressId());
            statement.setString(6, local.getPhoneNumber());
            statement.setString(7, local.getEmail());
            statement.setString(8, local.getType());
            statement.setString(9, local.getCategory());
            statement.setTime(10, Time.valueOf(local.getOpenHour().toLocalTime()));
            statement.setTime(11, Time.valueOf(local.getCloseHour().toLocalTime()));
            statement.setString(12, local.getStatus());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while creating local");
        }
    }

    public void update(Local local) throws Exception {
        String sqlQuery = "UPDATE locals SET administratorId = ?, name = ?, description = ?, addressId = ?, phoneNumber = ?, email = ?,  type = ?, category = ?, openHour = ?, closeHour = ?, status = ? WHERE id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, local.getAdministratorId());
            statement.setString(2, local.getName());
            statement.setString(3, local.getDescription());
            statement.setString(4, local.getAddressId());
            statement.setString(5, local.getPhoneNumber());
            statement.setString(6, local.getEmail());
            statement.setString(7, local.getType());
            statement.setString(8, local.getCategory());
            statement.setTime(9, Time.valueOf(local.getOpenHour().toLocalTime()));
            statement.setTime(10, Time.valueOf(local.getCloseHour().toLocalTime()));
            statement.setString(11, local.getStatus());
            statement.setString(12, local.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while updating local");
        }
    }

    public void delete(Local local) throws Exception {
        String sqlQuery = "DELETE FROM locals WHERE id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, local.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting local");
        }
    }
}
