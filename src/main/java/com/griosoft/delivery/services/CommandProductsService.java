package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.CommandProduct;
import main.java.com.griosoft.delivery.repositories.csv.CommandProductCSV;

import java.util.List;
import java.util.stream.Stream;

public class CommandProductsService {
    private final CommandProductCSV repository;

    public CommandProductsService() {
        this.repository = CommandProductCSV.getInstance();
    }

    public List<CommandProduct> getCommandProducts() {
        return repository.getCommandProducts();
    }

    public Stream<CommandProduct> getCommandProductsStream() {
        return repository.getCommandProducts().stream();
    }

    public void addCommandProduct(CommandProduct commandProduct) {
        repository.create(commandProduct);
    }

    public void addCommandProducts(List<CommandProduct> commandProducts) {
        repository.create(commandProducts);
    }

    public void updateCommandProduct(CommandProduct commandProduct) {
        repository.update(commandProduct);
    }

    public void deleteCommandProduct(CommandProduct commandProduct) {
        repository.delete(commandProduct);
    }
}
