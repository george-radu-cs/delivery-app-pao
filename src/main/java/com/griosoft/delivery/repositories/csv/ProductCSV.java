package main.java.com.griosoft.delivery.repositories.csv;

import main.java.com.griosoft.delivery.models.Product;
import main.java.com.griosoft.delivery.services.AuditService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProductCSV {
    private static final String CSV_FILE_PATH = "./src/main/resources/csv/products.csv";
    private static final String DELIMITER = ",";
    private static ProductCSV instance = null;
    private List<Product> products = new ArrayList<>();

    private ProductCSV() {
    }

    public static ProductCSV getInstance() {
        if (instance == null) {
            instance = new ProductCSV();
        }
        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Stream<String[]> readFromCSV() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CSV_FILE_PATH),
                StandardCharsets.UTF_8));
        return reader.lines().map(line -> line.split(DELIMITER));
    }

    public void loadFromCSV() {
        try {
            var productsEntriesStream = this.readFromCSV();
            productsEntriesStream
                    .filter(productEntry -> productEntry.length == 8)
                    .forEachOrdered(productEntry -> this.products.add(new Product.Builder()
                            .id(productEntry[0])
                            .localId(productEntry[1])
                            .name(productEntry[2])
                            .description(productEntry[3])
                            .price(productEntry[4])
                            .category(productEntry[5])
                            .quantity(productEntry[6])
                            .quantityMeasure(productEntry[7])
                            .build()));
        } catch (Exception e) {
            System.out.println("Error while loading products from CSV" + e.getMessage());
            AuditService.getInstance().log("Error while loading products from CSV" + e.getMessage());
        }
    }

    public void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILE_PATH),
                    StandardCharsets.UTF_8));
            for (var product : this.products) {
                writer.write(product.toCSV());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("Error while saving products to CSV" + e.getMessage());
            AuditService.getInstance().log("Error while saving products to CSV" + e.getMessage());
        }
    }
}
