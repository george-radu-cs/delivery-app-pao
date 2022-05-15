package main.java.com.griosoft.delivery.repositories;

import main.java.com.griosoft.delivery.config.DatabaseConnection;
import main.java.com.griosoft.delivery.models.CommandProduct;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommandProductRepository {
    public List<CommandProduct> getCommandProducts() throws Exception {
        List<CommandProduct> commandProducts = new ArrayList<>();
        String sqlQuery = "select * from command_products";
        try {
            Statement statement = DatabaseConnection.getInstance().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
            while (result.next()) {
                CommandProduct commandProduct = new CommandProduct.Builder()
                        .id(result.getString("id"))
                        .localId(result.getString("localId"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .price(result.getString("price"))
                        .category(result.getString("category"))
                        .quantity(result.getString("quantity"))
                        .quantityMeasure(result.getString("quantityMeasure"))
                        .commandId(result.getString("commandId"))
                        .numberOfProducts(result.getInt("numberOfProducts"))
                        .build();
                commandProducts.add(commandProduct);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while getting command products");
        }
        return commandProducts;
    }

    public void create(CommandProduct commandProduct) throws Exception {
        String sqlQuery = "insert into command_products values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, commandProduct.getId());
            statement.setString(2, commandProduct.getLocalId());
            statement.setString(3, commandProduct.getName());
            statement.setString(4, commandProduct.getDescription());
            statement.setString(5, commandProduct.getPrice());
            statement.setString(6, commandProduct.getCategory());
            statement.setString(7, commandProduct.getQuantity());
            statement.setString(8, commandProduct.getQuantityMeasure());
            statement.setString(9, commandProduct.getCommandId());
            statement.setInt(10, commandProduct.getNumberOfProducts());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while creating command product");
        }
    }

    public void create(List<CommandProduct> commandProducts) throws Exception {
        for (CommandProduct commandProduct : commandProducts) {
            this.create(commandProduct);
        }
    }

    public void update(CommandProduct commandProduct) throws Exception {
        String sqlQuery = "update command_products set localId = ?, name = ?, description = ?, price = ?, category = ?, quantity = ?, quantityMeasure = ?, commandId = ?, numberOfProducts = ? where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, commandProduct.getLocalId());
            statement.setString(2, commandProduct.getName());
            statement.setString(3, commandProduct.getDescription());
            statement.setString(4, commandProduct.getPrice());
            statement.setString(5, commandProduct.getCategory());
            statement.setString(6, commandProduct.getQuantity());
            statement.setString(7, commandProduct.getQuantityMeasure());
            statement.setString(8, commandProduct.getCommandId());
            statement.setInt(9, commandProduct.getNumberOfProducts());
            statement.setString(10, commandProduct.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while updating command product");
        }
    }

    public void delete(CommandProduct commandProduct) throws Exception {
        String sqlQuery = "delete from command_products where id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().prepareStatement(sqlQuery)) {
            statement.setString(1, commandProduct.getId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error while deleting command product");
        }
    }
}
