package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Customer;
import main.java.com.griosoft.delivery.models.User;
import main.java.com.griosoft.delivery.models.enums.UserType;
import main.java.com.griosoft.delivery.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class UserService {
    //    private final UserCSV repository;
    private final UserRepository repository;

    public UserService() {
//        repository = UserCSV.getInstance();
        this.repository = new UserRepository();
    }

    public List<User> getUsers() throws Exception {
        return repository.getUsers();
    }

    public Stream<User> getUsersStream() throws Exception {
        return repository.getUsers().stream();
    }

    public User getUserByEmail(String email) throws Exception {
        return this.getUsersStream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User getUserById(String id) throws Exception {
        return this.getUsersStream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public void updateUserPassword(String id, String password, UserType userType) throws Exception {
        User user = this.getUserById(id);
        user.setPassword(password);
        this.updateUser(user, userType);
    }

    public void updateCustomerCurrentAddressId(String customerId, String addressId) throws Exception {
        User newUser = this.getUserById(customerId);
        ((Customer) newUser).setCurrentAddressId(addressId);
        this.updateUser(newUser, UserType.CUSTOMER);
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getUserById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addUser(User user, UserType userType) throws Exception {
        repository.create(user, userType);
    }

    public void updateUser(User user, UserType userType) throws Exception {
        repository.update(user, userType);
    }

    public void deleteUser(User user) throws Exception {
        repository.delete(user);
    }
}

