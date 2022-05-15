package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerAddressRepository {
    public Map<String, List<String>> getCustomerAddressesIds() throws Exception {
        Map<String, List<String>> customerAddressesIds = new HashMap<>();
        String sqlQuery = "select * from customer_addresses";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                String customerId = result.getString("customerId");
                String addressId = result.getString("addressId");
                if (customerAddressesIds.containsKey(customerId)) {
                    customerAddressesIds.get(customerId).add(addressId);
                } else {
                    List<String> addresses = new ArrayList<>();
                    addresses.add(addressId);
                    customerAddressesIds.put(customerId, addresses);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting customer addresses");
        }
        return customerAddressesIds;
    }

    public void createCustomerAddressesList(String customerId) throws Exception {
        // nothing to do
    }

    public void addAddressToCustomer(String customerId, String addressId) throws Exception {
        String sqlQuery = "insert into customer_addresses values (?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, customerId);
            statement.setString(2, addressId);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while adding address to customer");
        }
    }

    public void deleteAddressFromCustomer(String customerId, String addressId) throws Exception {
        String sqlQuery = "delete from customer_addresses where customerId = ? and addressId = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, customerId);
            statement.setString(2, addressId);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting address from customer");
        }
    }
}
