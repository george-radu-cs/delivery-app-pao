package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.Customer;
import main.java.com.griosoft.delivery.models.DeliveryPerson;
import main.java.com.griosoft.delivery.models.LocalAdministrator;
import main.java.com.griosoft.delivery.models.User;
import main.java.com.griosoft.delivery.models.enums.TransportType;
import main.java.com.griosoft.delivery.models.enums.UserType;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/users.csv";
    private static final String DELIMITER = ",";
    private static UserCSV instance = null;
    private final List<User> users = new ArrayList<>();

    private UserCSV() {
    }

    public static UserCSV getInstance() {
        if (instance == null) {
            instance = new UserCSV();
        }
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void create(User user) {
        this.users.add(user);
    }

    public void update(User user) {
        users.set(users.indexOf(user), user);
    }

    public void delete(User user) {
        users.remove(user);
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var userEntriesStream = this.readFromCSV();
            userEntriesStream.forEachOrdered(userEntry -> {
                if (userEntry.length != 0) {
                    User.Builder userBuilder = null;
                    switch (UserType.getUserType(userEntry[0])) {
                        case CUSTOMER -> {
                            userBuilder = new Customer.Builder();
                            ((Customer.Builder) userBuilder).currentAddressId(userEntry[7]);
                        }
                        case DELIVERY_PERSON -> {
                            userBuilder = new DeliveryPerson.Builder();
                            ((DeliveryPerson.Builder) userBuilder).transportType(TransportType.getTransportType(userEntry[7]));
                        }
                        case LOCAL_ADMINISTRATOR -> {
                            userBuilder = new LocalAdministrator.Builder();
                            ((LocalAdministrator.Builder) userBuilder).administratorCertificate(userEntry[7]);
                        }
                        case UNKNOWN -> {
                            // don't throw an error if a user is saved with an unknown type
                        }
                    }
                    if (userBuilder != null) {
                        this.users.add(userBuilder
                                .id(userEntry[1])
                                .firstName(userEntry[2])
                                .lastName(userEntry[3])
                                .email(userEntry[4])
                                .phoneNumber(userEntry[5])
                                .password(userEntry[6])
                                .build());
                    }  // if a user is saved with an unknown type, skip it
                }
            });
        } catch (Exception e) {
            System.out.println("Error while loading users from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading users from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var user : users) {
                writer.write(user.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving users to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving users to CSV" + e.getMessage());
        }
    }
}
