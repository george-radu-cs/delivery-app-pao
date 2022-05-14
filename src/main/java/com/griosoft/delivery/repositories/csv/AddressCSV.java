package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.Address;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class AddressCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/addresses.csv";
    private static final String DELIMITER = ",";
    private static AddressCSV instance = null;
    private final List<Address> addresses = new ArrayList<>();

    private AddressCSV() {
    }

    public static AddressCSV getInstance() {
        if (instance == null) {
            instance = new AddressCSV();
        }
        return instance;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void create(Address address) {
        this.addresses.add(address);
    }

    public void update(Address address) {
        addresses.set(addresses.indexOf(address), address);
    }

    public void delete(Address address) {
        addresses.remove(address);
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var addressEntriesStream = this.readFromCSV();
            addressEntriesStream
                    .filter(addressEntry -> addressEntry.length == 7)
                    .forEachOrdered(addressEntry -> this.addresses.add(new Address.Builder()
                            .id(addressEntry[0])
                            .number(parseInt(addressEntry[1]))
                            .street(addressEntry[2])
                            .city(addressEntry[3])
                            .state(addressEntry[4])
                            .zipCode(addressEntry[5])
                            .build()));
        } catch (Exception e) {
            System.out.println("Error while loading addresses from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading addresses from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var address : this.addresses) {
                writer.write(address.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving addresses to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving addresses to CSV" + e.getMessage());
        }
    }
}
