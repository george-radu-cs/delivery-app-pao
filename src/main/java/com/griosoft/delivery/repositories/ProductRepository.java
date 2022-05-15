package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Product> getProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        String sqlQuery = "select * from products";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                Product product = new Product.Builder()
                        .id(result.getString("id"))
                        .localId(result.getString("localId"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .price(result.getString("price"))
                        .category(result.getString("category"))
                        .quantity(result.getString("quantity"))
                        .quantityMeasure(result.getString("quantityMeasure"))
                        .build();
                products.add(product);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting products");
        }
        return products;
    }

    public void create(Product product) throws Exception {
        String sqlQuery = "insert into products values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, product.getId());
            statement.setString(2, product.getLocalId());
            statement.setString(3, product.getName());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getPrice());
            statement.setString(6, product.getCategory());
            statement.setString(7, product.getQuantity());
            statement.setString(8, product.getQuantityMeasure());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while creating product");
        }
    }

    public void update(Product product) throws Exception {
        String sqlQuery = "update products set localId = ?, name = ?, description = ?, price = ?, category = ?, quantity = ?, quantityMeasure = ? where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, product.getLocalId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getPrice());
            statement.setString(5, product.getCategory());
            statement.setString(6, product.getQuantity());
            statement.setString(7, product.getQuantityMeasure());
            statement.setString(8, product.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while updating product");
        }
    }

    public void delete(Product product) throws Exception {
        String sqlQuery = "delete from products where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, product.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting product");
        }
    }
}
