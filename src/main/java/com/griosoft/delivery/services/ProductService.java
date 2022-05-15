package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Product;
import main.java.com.griosoft.delivery.repositories.ProductRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ProductService {
    //    private final ProductCSV repository;
    private final ProductRepository repository;

    public ProductService() {
//        repository = ProductCSV.getInstance();
        this.repository = new ProductRepository();
    }

    public List<Product> getProducts() throws Exception {
        return repository.getProducts();
    }

    public Stream<Product> getProductsStream() throws Exception {
        return repository.getProducts().stream();
    }

    public Product getProductById(String id) throws Exception {
        return this.getProductsStream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Product> getProductsByLocalId(String localId) throws Exception {
        return this.getProductsStream().filter(product -> product.getLocalId().equals(localId));
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getProductById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addProduct(Product product) throws Exception {
        repository.create(product);
    }

    public void updateProduct(Product product) throws Exception {
        repository.update(product);
    }

    public void deleteProduct(Product product) throws Exception {
        repository.delete(product);
    }
}
