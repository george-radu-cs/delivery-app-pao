package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CustomerAddressesCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/customer_addresses.csv";
    private static final String DELIMITER = ",";
    private static CustomerAddressesCSV instance = null;
    private Map<String, List<String>> customerAddressesIds = new HashMap<>();

    private CustomerAddressesCSV() {
    }

    public static CustomerAddressesCSV getInstance() {
        if (instance == null) {
            instance = new CustomerAddressesCSV();
        }
        return instance;
    }

    public Map<String, List<String>> getCustomerAddressesIds() {
        return customerAddressesIds;
    }

    public void setCustomerAddressesIds(Map<String, List<String>> customerAddressesIds) {
        this.customerAddressesIds = customerAddressesIds;
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var customerAddressesEntriesStream = this.readFromCSV();
            customerAddressesEntriesStream.forEachOrdered(customerAddressesEntry -> {
                if (customerAddressesEntry.length == 2) {
                    if (!this.customerAddressesIds.containsKey(customerAddressesEntry[0])) {
                        this.customerAddressesIds.put(customerAddressesEntry[0], new ArrayList<>());
                    }
                    this.customerAddressesIds.get(customerAddressesEntry[0]).add(customerAddressesEntry[1]);
                }
            });

        } catch (Exception e) {
            System.out.println("Error while loading customer addresses from CSV" + e.getMessage());
            AuditService.getInstance().log(e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            this.customerAddressesIds.forEach((customerId, addressesIds) -> {
                try {
                    for (String addressId : addressesIds) {
                        writer.write(customerId + DELIMITER + addressId + "\n");
                    }
                } catch (Exception e) {
                    System.out.println("Error while saving customer addresses to CSV" + e.getMessage());
                    AuditService.getInstance().log(e.getMessage());
                }
            });
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving customer addresses to CSV" + e.getMessage());
            AuditService.getInstance().log(e.getMessage());
        }
    }
}
