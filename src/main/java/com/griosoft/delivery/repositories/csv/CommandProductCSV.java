package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.CommandProduct;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class CommandProductCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/command_products.csv";
    private static final String DELIMITER = ",";
    private static CommandProductCSV instance = null;
    private List<CommandProduct> commandProducts = new ArrayList<>();

    private CommandProductCSV() {
    }

    public static CommandProductCSV getInstance() {
        if (instance == null) {
            instance = new CommandProductCSV();
        }
        return instance;
    }

    public List<CommandProduct> getCommandProducts() {
        return commandProducts;
    }

    public void setCommandProducts(List<CommandProduct> commandProducts) {
        this.commandProducts = commandProducts;
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var commandProductsEntriesStream = this.readFromCSV();
            commandProductsEntriesStream
                    .filter(commandProductEntry -> commandProductEntry.length == 10)
                    .forEachOrdered(commandProductEntry -> this.commandProducts.add(new CommandProduct.Builder()
                            .id(commandProductEntry[0])
                            .localId(commandProductEntry[1])
                            .name(commandProductEntry[2])
                            .description(commandProductEntry[3])
                            .price(commandProductEntry[4])
                            .category(commandProductEntry[5])
                            .quantity(commandProductEntry[6])
                            .quantityMeasure(commandProductEntry[7])
                            .commandId(commandProductEntry[8])
                            .numberOfProducts(parseInt(commandProductEntry[9]))
                            .build()));
        } catch (Exception e) {
            System.out.println("Error while loading command products from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading command products from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var commandProduct : this.commandProducts) {
                writer.write(commandProduct.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving command products to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving command products to CSV" + e.getMessage());
        }
    }
}
