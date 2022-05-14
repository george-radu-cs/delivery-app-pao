package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Product;
import main.java.com.griosoft.delivery.repositories.csv.ProductCSV;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ProductService {
    ProductCSV repository;

    public ProductService() {
        repository = ProductCSV.getInstance();
    }

    public List<Product> getProducts() {
        return repository.getProducts();
    }

    public Stream<Product> getProductsStream() {
        return repository.getProducts().stream();
    }

    public Product getProductById(String id) {
        return this.getProductsStream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Product> getProductsByLocalId(String localId) {
        return this.getProductsStream().filter(product -> product.getLocalId().equals(localId));
    }

    public String getNewUUID() {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getProductById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addProduct(Product product) {
        repository.create(product);
    }

    public void updateProduct(Product product) {
        repository.update(product);
    }

    public void deleteProduct(Product product) {
        repository.delete(product);
    }
}
