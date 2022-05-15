package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.CommandProduct;
import main.java.com.griosoft.delivery.repositories.CommandProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandProductsService {
    //    private final CommandProductCSV repository;
    private final CommandProductRepository repository;

    public CommandProductsService() {
//        this.repository = CommandProductCSV.getInstance();
        this.repository = new CommandProductRepository();
    }

    public List<CommandProduct> getCommandProducts() throws Exception {
        return repository.getCommandProducts();
    }

    public CommandProduct getCommandProductById(String id) throws Exception {
        return this.getCommandProducts().stream().filter(commandProduct -> commandProduct.getId().equals(id))
                   .findFirst().orElse(null);
    }

    public Stream<CommandProduct> getCommandProductsStream() throws Exception {
        return repository.getCommandProducts().stream();
    }

    public List<CommandProduct> getCommandProductsByCommandId(String commandId) throws Exception {
        return this.getCommandProductsStream().filter(commandProduct -> commandProduct.getCommandId().equals(commandId))
                   .collect(Collectors.toList());
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getCommandProductById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addCommandProduct(CommandProduct commandProduct) throws Exception {
        repository.create(commandProduct);
    }

    public void addCommandProducts(List<CommandProduct> commandProducts) throws Exception {
        repository.create(commandProducts);
    }

    public void updateCommandProduct(CommandProduct commandProduct) throws Exception {
        repository.update(commandProduct);
    }

    public void deleteCommandProduct(CommandProduct commandProduct) throws Exception {
        repository.delete(commandProduct);
    }
}
