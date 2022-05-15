package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.Customer;
import main.java.com.griosoft.delivery.models.DeliveryPerson;
import main.java.com.griosoft.delivery.models.LocalAdministrator;
import main.java.com.griosoft.delivery.models.User;
import main.java.com.griosoft.delivery.models.enums.TransportType;
import main.java.com.griosoft.delivery.models.enums.UserType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    public List<User> getUsers() throws Exception {
        List<User> users = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                User.Builder userBuilder = null;
                switch (UserType.getUserType(result.getString("userType"))) {
                    case CUSTOMER -> {
                        userBuilder = new Customer.Builder();
                        ((Customer.Builder) userBuilder).currentAddressId(result.getString("currentAddressId"));
                    }
                    case DELIVERY_PERSON -> {
                        userBuilder = new DeliveryPerson.Builder();
                        ((DeliveryPerson.Builder) userBuilder).transportType(TransportType.getTransportType(result.getString("transportType")));
                    }
                    case LOCAL_ADMINISTRATOR -> {
                        userBuilder = new LocalAdministrator.Builder();
                        ((LocalAdministrator.Builder) userBuilder).administratorCertificate(result.getString("administratorCertificate"));
                    }
                    case UNKNOWN -> {
                        // don't throw an error if a user is saved with an unknown type
                    }
                }
                Objects.requireNonNull(userBuilder)
                       .id(result.getString("id"))
                       .firstName(result.getString("firstName"))
                       .lastName(result.getString("lastName"))
                       .email(result.getString("email"))
                       .phoneNumber(result.getString("phoneNumber"))
                       .password(result.getString("password"));
                users.add(userBuilder.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting users");
        }
        return users;
    }

    public void create(User user, UserType userType) throws Exception {
        String sqlQuery = "insert into users values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getPassword());
            statement.setString(7, userType.toString());
            switch (userType) {
                case CUSTOMER -> {
                    statement.setString(8, ((Customer) user).getCurrentAddressId());
                    statement.setString(9, null);
                    statement.setString(10, null);
                }
                case DELIVERY_PERSON -> {
                    statement.setString(9, ((DeliveryPerson) user).getTransportType().toString());
                    statement.setString(8, null);
                    statement.setString(10, null);
                }
                case LOCAL_ADMINISTRATOR -> {
                    statement.setString(10, ((LocalAdministrator) user).getAdministratorCertificate());
                    statement.setString(8, null);
                    statement.setString(9, null);
                }
                case UNKNOWN -> throw new Exception("Unknown user type");
            }
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while creating user");
        }
    }

    public void update(User user, UserType userType) throws Exception {
        String sqlQuery = "update users set firstName = ?, lastName = ?, email = ?, phoneNumber = ?, password = ?, userType = ?, currentAddressId = ?, transportType = ?, administratorCertificate = ? where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getPassword());
            statement.setString(6, userType.toString());
            switch (userType) {
                case CUSTOMER -> {
                    statement.setString(7, ((Customer) user).getCurrentAddressId());
                    statement.setString(8, null);
                    statement.setString(9, null);
                }
                case DELIVERY_PERSON -> {
                    statement.setString(8, ((DeliveryPerson) user).getTransportType().toString());
                    statement.setString(7, null);
                    statement.setString(9, null);
                }
                case LOCAL_ADMINISTRATOR -> {
                    statement.setString(9, ((LocalAdministrator) user).getAdministratorCertificate());
                    statement.setString(7, null);
                    statement.setString(8, null);
                }
                case UNKNOWN -> throw new Exception("Unknown user type");
            }
            statement.setString(10, user.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while updating user");
        }
    }

    public void delete(User user) throws Exception {
        String sqlQuery = "delete from users where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting user");
        }
    }
}
