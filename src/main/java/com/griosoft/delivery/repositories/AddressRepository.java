package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressRepository {
    public List<Address> getAddresses() throws Exception {
        List<Address> addresses = new ArrayList<>();
        String sqlQuery = "select * from addresses";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                Address address = new Address.Builder()
                        .id(result.getString("id"))
                        .number(result.getInt("number"))
                        .street(result.getString("street"))
                        .city(result.getString("city"))
                        .state(result.getString("state"))
                        .country(result.getString("country"))
                        .zipCode(result.getString("zipCode"))
                        .build();
                addresses.add(address);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting addresses");
        }
        return addresses;
    }

    public void create(Address address) throws Exception {
        String sqlQuery = "insert into addresses values (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, address.getId());
            statement.setInt(2, address.getNumber());
            statement.setString(3, address.getStreet());
            statement.setString(4, address.getCity());
            statement.setString(5, address.getState());
            statement.setString(6, address.getCountry());
            statement.setString(7, address.getZipCode());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while creating address");
        }
    }

    public void update(Address address) throws Exception {
        String sqlQuery = "update addresses set number = ?, street = ?, city = ?, state = ?, country = ?, zipCode = ? where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setInt(1, address.getNumber());
            statement.setString(2, address.getStreet());
            statement.setString(3, address.getCity());
            statement.setString(4, address.getState());
            statement.setString(5, address.getCountry());
            statement.setString(6, address.getZipCode());
            statement.setString(7, address.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while updating address");
        }
    }

    public void delete(Address address) throws Exception {
        String sqlQuery = "delete from addresses where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, address.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting address");
        }
    }
}
