package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Customer;
import main.java.com.griosoft.delivery.models.User;
import main.java.com.griosoft.delivery.repositories.csv.UserCSV;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class UserService {
    UserCSV repository;

    public UserService() {
        repository = UserCSV.getInstance();
    }

    public List<User> getUsers() {
        return repository.getUsers();
    }

    public Stream<User> getUsersStream() {
        return repository.getUsers().stream();
    }

    public User getUserByEmail(String email) {
        return this.getUsersStream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User getUserById(String id) {
        return this.getUsersStream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public void updateUserPassword(String id, String password) {
        User user = this.getUserById(id);
        user.setPassword(password);
        this.updateUser(user);
    }

    public void updateCustomerCurrentAddressId(String customerId, String addressId) {
        ((Customer) this.getUserById(customerId)).setCurrentAddressId(addressId);
        this.updateUser(this.getUserById(customerId));
    }

    public String getNewUUID() {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getUserById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addUser(User user) {
        repository.create(user);
    }

    public void updateUser(User user) {
        repository.update(user);
    }

    public void deleteUser(User user) {
        // TODO create in main service option to delete user
        repository.delete(user);
    }
}

