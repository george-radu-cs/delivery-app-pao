package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.exceptions.LoggedUserTypeException;
import main.java.com.griosoft.delivery.models.*;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;
import main.java.com.griosoft.delivery.models.enums.TransportType;
import main.java.com.griosoft.delivery.models.enums.UserType;
import main.java.com.griosoft.delivery.utils.UserCommandsDescriptors;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainService {
    private static final MainService instance = new MainService();
    private static final Integer MAX_ENTRIES = 10;
    private List<Address> addresses = new ArrayList<>();
    private Map<String, List<String>> customerAddressesIds = new HashMap<>();
    private List<Command> commands = new ArrayList<>();
    private List<CommandProduct> commandProducts = new ArrayList<>();
    private List<Local> locals = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private User loggedUser = null;
    private UserType loggedUserType = null;

    private MainService() {
    }

    public static MainService getInstance() {
        return instance;
    }

    public void test() throws Exception {
        // print users
        System.out.println(loggedUserType);
    }

    // GETTERS AND SETTERS
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Map<String, List<String>> getCustomerAddressesIds() {
        return customerAddressesIds;
    }

    public void setCustomerAddressesIds(Map<String, List<String>> customerAddressesIds) {
        this.customerAddressesIds = customerAddressesIds;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public List<CommandProduct> getCommandProducts() {
        return commandProducts;
    }

    public void setCommandProducts(List<CommandProduct> commandProducts) {
        this.commandProducts = commandProducts;
    }

    public List<Local> getLocals() {
        return locals;
    }

    public void setLocals(List<Local> locals) {
        this.locals = locals;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    private void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public UserType getLoggedUserType() {
        return loggedUserType;
    }

    private void setLoggedUserType(UserType loggedUserType) {
        this.loggedUserType = loggedUserType;
    }

    public void printHelp() {
        System.out.println("Available commands:");
        if (this.loggedUser == null) {
            UserCommandsDescriptors.getInstance().getUserNotConnectedCommands()
                                   .forEach((userCommand, description) -> System.out.println(userCommand + " - " + description));
        } else {
            UserCommandsDescriptors.getInstance().getUserConnectedCommands()
                                   .forEach((userCommand, description) -> System.out.println(userCommand + " - " + description));
            UserCommandsDescriptors.getInstance().getUserCommandsDetails().get(this.loggedUserType)
                                   .forEach((userCommand, description) -> System.out.println(userCommand + " - " + description));
        }
    }

    // users service
    public void register(Scanner scanner) throws Exception {
        // create a uuid and check to not exist already
        String uniqueId = UUID.randomUUID().toString();
        while (getUserById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        System.out.println("Enter your email");
        String email = scanner.nextLine();
        System.out.println("Enter your password");
        String password = scanner.nextLine();
        System.out.println("Enter your first name");
        String firstName = scanner.nextLine();
        System.out.println("Enter your last name");
        String lastName = scanner.nextLine();
        System.out.println("Enter your phone number");
        String phoneNumber = scanner.nextLine();

        System.out.println("What type of user do you want to register?");
        User.Builder userToRegisterBuilder = null;
        UserType userType = UserType.UNKNOWN;
        while (userType == UserType.UNKNOWN) {
            System.out.println("Available types:");
            UserType.printUserTypes();
            userType = UserType.getUserType(scanner.nextLine());
            switch (userType) {
                case CUSTOMER -> {
                    userToRegisterBuilder = new Customer.Builder();
                }
                case DELIVERY_PERSON -> {
                    userToRegisterBuilder = new DeliveryPerson.Builder();
                    System.out.println("Transport types available:");
                    TransportType.printTransportTypes();
                    TransportType transportType = TransportType.getTransportType(scanner.nextLine());
                    ((DeliveryPerson.Builder) userToRegisterBuilder).transportType(transportType);
                }
                case LOCAL_ADMINISTRATOR -> {
                    userToRegisterBuilder = new LocalAdministrator.Builder();
                    System.out.println("Enter your administrator certificate number");
                    String administratorCertificate = scanner.nextLine();
                    ((LocalAdministrator.Builder) userToRegisterBuilder).administratorCertificate(administratorCertificate);
                }
                case UNKNOWN -> throw new Exception("Unknown user type");
            }
        }

        User user = userToRegisterBuilder
                .id(uniqueId)
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .build();
        this.addUser(user);
    }

    public void login(Scanner scanner) throws Exception {
        if (this.loggedUser != null) {
            throw new IllegalStateException("You are already logged in");
        }
        System.out.println("Enter your email");
        String email = scanner.nextLine();
        System.out.println("Enter your password");
        String password = scanner.nextLine();
        User user = this.getUserByEmail(email);
        if (user == null) {
            throw new Exception("User with email " + email + " not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("Wrong password");
        }
        this.setLoggedUser(user);
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName());
        AuditService.getInstance().log("User " + user.getId() + " logged in");

        this.setLoggedUserType(UserType.getUserType(user.getClass().getSimpleName()));
    }

    public void logout() throws Exception {
        if (this.loggedUser != null) {
            System.out.println("Logged out");
            AuditService.getInstance().log("User " + this.getLoggedUser().getId() + " logged out");
            this.setLoggedUser(null);
            this.setLoggedUserType(null);
        } else {
            throw new Exception("You are not logged in");
        }
    }

    public void changePassword(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        System.out.println("Enter your old password");
        String oldPassword = scanner.nextLine();
        if (!this.loggedUser.getPassword().equals(oldPassword)) {
            throw new Exception("Wrong password");
        }
        System.out.println("Enter your new password");
        String newPassword = scanner.nextLine();
        System.out.println("Confirm your new password");
        String confirmPassword = scanner.nextLine();
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }
        if (newPassword.equals(oldPassword)) {
            throw new Exception("New password must be different from old password");
        }
        this.loggedUser.setPassword(newPassword);
    }

    public User getUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User getUserById(String id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void updateUser(User user) {
        users.set(users.indexOf(user), user);
    }

    // customer commands
    public void seeListOfLocals(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();
        this.locals.stream().limit(MAX_ENTRIES).forEach(System.out::println);
        int numberOfLocalsPrinted = MAX_ENTRIES;
        while (numberOfLocalsPrinted < this.locals.size()) {
            System.out.println("...more locals...");
            System.out.println("Do you want to see more locals? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                this.locals.stream().skip(numberOfLocalsPrinted).limit(MAX_ENTRIES).forEach(System.out::println);
                numberOfLocalsPrinted += MAX_ENTRIES;
            } else {
                break;
            }
        }
    }

    public void seeLocalInfo(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomerOrLocalAdministrator();
        System.out.println("Enter the id of the local you want to see");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        if (this.loggedUserType == UserType.LOCAL_ADMINISTRATOR &&
                !local.getAdministratorId().equals(this.loggedUser.getId())) {
            throw new IllegalStateException("You are not the administrator of this local");
        }
        System.out.println(local);
        System.out.println("Products:");
        this.getProductsByLocalId(localId).forEach(System.out::println);
    }

    public void createCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the local you want to order from");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }

        String uniqueId = UUID.randomUUID().toString();
        while (this.getCommandById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }

        List<Product> localProducts = this.getProductsByLocalId(localId).toList();
        System.out.println("Products for local " + local.getName() + ":");
        localProducts.forEach(System.out::println);

        List<CommandProduct> commandProducts = new ArrayList<>();
        while (true) {
            System.out.println("Enter the id of the product you want to order");
            String productId = scanner.nextLine();
            Product product = localProducts.stream().filter(p -> p.getId().equals(productId)).findFirst().orElse(null);
            if (product == null) {
                throw new Exception("Product with id " + productId + " not found");
            }
            System.out.println("Enter the number of products you want to order");
            int numberOfProducts = Integer.parseInt(scanner.nextLine());
            while (numberOfProducts <= 0) {
                System.out.println("Number of products must be greater than 0");
                numberOfProducts = Integer.parseInt(scanner.nextLine());
            }
            commandProducts.add(new CommandProduct.Builder()
                    .id(product.getId())
                    .localId(product.getLocalId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .category(product.getCategory())
                    .quantity(product.getQuantity())
                    .quantityMeasure(product.getQuantityMeasure())
                    .commandId(uniqueId)
                    .numberOfProducts(numberOfProducts)
                    .build());

            System.out.println("Do you want to order more products? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("n")) {
                break;
            }
        }

        this.commandProducts.addAll(commandProducts);
        this.commands.add(new Command.Builder()
                .id(uniqueId)
                .customerId(loggedUser.getId())
                .localId(localId)
                .createdAt(OffsetDateTime.now())
                .deliveredAt(null)
                .status(CommandStatus.PENDING)
                .build());
    }

    public void cancelCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the command you want to cancel");
        String commandId = scanner.nextLine();
        Command command = this.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getCustomerId().equals(this.loggedUser.getId())) {
            if (command.getStatus() == CommandStatus.PENDING) {
                command.setStatus(CommandStatus.CANCELLED);
                this.commands.set(this.commands.indexOf(command), command);
//                this.commands.remove(command);
            }
        }
    }

    public void seeMyCommands(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Your commands:");
        this.getCommandsByCustomerId(this.loggedUser.getId()).limit(MAX_ENTRIES).forEach(System.out::println);
        var numberOfCommands = this.getCommandsByCustomerId(this.loggedUser.getId()).count();
        int numberOfCommandsPrinted = MAX_ENTRIES;
        while (numberOfCommandsPrinted < numberOfCommands) {
            System.out.println("...more commands...");
            System.out.println("Do you want to see more commands? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                this.getCommandsByCustomerId(this.loggedUser.getId()).skip(numberOfCommandsPrinted).limit(MAX_ENTRIES)
                    .forEach(System.out::println);
                numberOfCommandsPrinted += MAX_ENTRIES;
            } else {
                break;
            }
        }
    }

    public void seeMyAddresses(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Your addresses:");
        this.getAddressesByCustomerId(this.loggedUser.getId()).forEach(System.out::println);
    }

    public void addAddress(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        String addressId = this.createAddress(scanner);
        if (!this.customerAddressesIds.containsKey(this.loggedUser.getId())) {
            this.customerAddressesIds.put(this.loggedUser.getId(), new ArrayList<>());
            this.updateCustomerCurrentAddressId(this.loggedUser.getId(), addressId);
        }
        this.customerAddressesIds.get(this.loggedUser.getId()).add(addressId);
    }

    public void changeCurrentAddress(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the address you want to use as your current address");
        String addressId = scanner.nextLine();
        String address = this.getCustomerAddressesIds().get(this.loggedUser.getId()).stream()
                             .filter(a -> a.equals(addressId)).findFirst().orElse(null);
        if (address == null) {
            throw new Exception("Address with id " + addressId + " not found");
        }
        this.updateCustomerCurrentAddressId(this.loggedUser.getId(), addressId);
    }

    // delivery person commands
    public void seeCommandsAvailableForDelivery(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Commands available for delivery:");
        this.getActiveCommands().forEach(System.out::println);
    }

    public void acceptCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Enter the id of the command you want to accept");
        String commandId = scanner.nextLine();
        Command command = this.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getStatus() == CommandStatus.PENDING) {
            command.setStatus(CommandStatus.DELIVERY_ACCEPTED);
            this.commands.set(this.commands.indexOf(command), command);
        }
    }

    public void deliverCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Enter the id of the command you want to deliver");
        String commandId = scanner.nextLine();
        Command command = this.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getStatus() == CommandStatus.DELIVERY_ACCEPTED) {
            command.setStatus(CommandStatus.DELIVERED);
            command.setDeliveredAt(OffsetDateTime.now());
            this.commands.set(this.commands.indexOf(command), command);
        }
    }

    // local administrator commands
    public void addLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        String uniqueId = UUID.randomUUID().toString();
        while (this.getLocalById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        System.out.println("Enter the name of the local");
        String name = scanner.nextLine();
        System.out.println("Enter the description of the local");
        String description = scanner.nextLine();
        System.out.println("Enter the address of the local");
        String addressId = this.createAddress(scanner);
        System.out.println("Enter the phone of the local");
        String phone = scanner.nextLine();
        System.out.println("Enter the email of the local");
        String email = scanner.nextLine();
        System.out.println("Enter the type of the local");
        String type = scanner.nextLine();
        System.out.println("Enter the category of the local");
        String category = scanner.nextLine();
        System.out.println("Enter the open hour of the local in the format HH:mm:ss+HH:mm");
        OffsetTime openHour = OffsetTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_TIME);
        System.out.println("Enter the close hour of the local in the format HH:mm:ss+HH:mm");
        OffsetTime closeHour = OffsetTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_TIME);
        String status = "ACTIVE";

        this.locals.add(new Local.Builder()
                .id(uniqueId)
                .administratorId(this.loggedUser.getId())
                .name(name)
                .description(description)
                .addressId(addressId)
                .phoneNumber(phone)
                .email(email)
                .openHour(openHour)
                .closeHour(closeHour)
                .type(type)
                .category(category)
                .status(status)
                .build());
        System.out.println("Local added");
    }

    public void updateLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to update");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        System.out.println("Current local information: " + local);

        System.out.println("Enter the name of the local");
        String name = scanner.nextLine();
        System.out.println("Enter the description of the local");
        String description = scanner.nextLine();
        System.out.println("Enter the address of the local");
        String addressId = this.createAddress(scanner);
        System.out.println("Enter the phone of the local");
        String phone = scanner.nextLine();
        System.out.println("Enter the email of the local");
        String email = scanner.nextLine();
        System.out.println("Enter the type of the local");
        String type = scanner.nextLine();
        System.out.println("Enter the category of the local");
        String category = scanner.nextLine();
        System.out.println("Enter the open hour of the local in the format HH:mm:ss+HH:mm");
        OffsetTime openHour = OffsetTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_TIME);
        System.out.println("Enter the close hour of the local in the format HH:mm:ss+HH:mm");
        OffsetTime closeHour = OffsetTime.parse(scanner.nextLine(), DateTimeFormatter.ISO_TIME);
        String status = "ACTIVE";

        this.updateLocal(new Local.Builder()
                .id(localId)
                .administratorId(this.loggedUser.getId())
                .name(name)
                .description(description)
                .addressId(addressId)
                .phoneNumber(phone)
                .email(email)
                .openHour(openHour)
                .closeHour(closeHour)
                .type(type)
                .category(category)
                .status(status)
                .build());
        System.out.println("Local updated");
    }

    public void deleteLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to delete");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.removeLocal(local);
        System.out.println("Local deleted");
    }

    public void seeMyLocals(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        var userLocals = this.getLocalsByAdministratorId(this.loggedUser.getId()).toList();
        IntStream.range(0, MAX_ENTRIES).forEach(i -> System.out.println(userLocals.get(i)));
        int numberOfLocalsPrinted = MAX_ENTRIES;
        while (numberOfLocalsPrinted < userLocals.size()) {
            System.out.println("...more locals...");
            System.out.println("Do you want to see more locals? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                try {
                    IntStream.range(numberOfLocalsPrinted, numberOfLocalsPrinted + MAX_ENTRIES)
                             .forEach(i -> System.out.println(userLocals.get(i)));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("No more locals");
                }
                numberOfLocalsPrinted += MAX_ENTRIES;
            } else {
                break;
            }
        }
    }

    public void seeListOfProductsForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to see the products of");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.getProductsByLocalId(localId).forEach(System.out::println);
    }

    public void addProductForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        String uniqueId = UUID.randomUUID().toString();
        while (this.getProductById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        System.out.println("Enter the id of the local you want to add the product to");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        System.out.println("Enter the name of the product");
        String name = scanner.nextLine();
        System.out.println("Enter the description of the product");
        String description = scanner.nextLine();
        System.out.println("Enter the price of the product");
        String price = scanner.nextLine();
        System.out.println("Enter the category of the product");
        String category = scanner.nextLine();
        System.out.println("Enter the quantity of the product");
        String quantity = scanner.nextLine();
        System.out.println("Enter the quantity measure of the product");
        String quantityMeasure = scanner.nextLine();
        this.products.add(new Product.Builder()
                .id(uniqueId)
                .localId(localId)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .quantity(quantity)
                .quantityMeasure(quantityMeasure)
                .build());
    }

    public void seeCommandsForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to see the commands of");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.getCommandsByLocalId(localId).forEach(System.out::println);
    }

    public void seeActiveCommandsForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to see the active commands of");
        String localId = scanner.nextLine();
        Local local = this.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.getCommandsByLocalId(localId).filter(command -> command.getStatus().equals(CommandStatus.PENDING))
            .forEach(System.out::println);
    }

    // METHODS FOR SERVICES
    public Address getAddressById(String id) {
        return this.addresses.stream().filter(address -> address.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Address> getAddressesByCustomerId(String customerId) {
//        return this.addresses.stream().filter(address -> address.getCustomerId().equals(customerId));
        var customerAddressesIds = this.customerAddressesIds.get(customerId);
        return this.addresses.stream().filter(address -> customerAddressesIds.contains(address.getId()));
    }

    public Local getLocalById(String id) {
        return locals.stream().filter(local -> local.getId().equals(id)).findFirst().orElse(null);
    }

    public Product getProductById(String id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Product> getProductsByLocalId(String localId) {
        return products.stream().filter(product -> product.getLocalId().equals(localId));
    }

    public Stream<Command> getCommandsByLocalId(String localId) {
        return commands.stream().filter(command -> command.getLocalId().equals(localId));
    }

    public Stream<Command> getCommandsByCustomerId(String customerId) {
        return commands.stream().filter(command -> command.getCustomerId().equals(customerId));
    }

    public Stream<Command> getActiveCommands() {
        return commands.stream().filter(command -> command.getStatus().equals(CommandStatus.PENDING));
    }

    public Command getCommandById(String id) {
        return commands.stream().filter(command -> command.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Local> getLocalsByAdministratorId(String administratorId) {
        return locals.stream().filter(local -> local.getAdministratorId().equals(administratorId));
    }

    public void addLocal(Local local) {
        locals.add(local);
    }

    public void updateLocal(Local local) {
        locals.removeIf(l -> l.getId().equals(local.getId()));
        locals.add(local);
    }

    public void removeLocal(Local local) {
        locals.remove(local);
    }

    public void updateCustomerCurrentAddressId(String customerId, String addressId) {
        ((Customer) this.getUserById(customerId)).setCurrentAddressId(addressId);
        this.updateUser(this.getUserById(customerId));
    }

    public String createAddress(Scanner scanner) throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getAddressById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        System.out.println("Enter the number of the address");
        Integer number = Integer.valueOf(scanner.nextLine());
        System.out.println("Enter the street of the address");
        String street = scanner.nextLine();
        System.out.println("Enter the city of the address");
        String city = scanner.nextLine();
        System.out.println("Enter the state of the address");
        String state = scanner.nextLine();
        System.out.println("Enter the country of the address");
        String country = scanner.nextLine();
        System.out.println("Enter the zip code of the address");
        String zipCode = scanner.nextLine();
        this.addresses.add(new Address.Builder()
                .id(uniqueId)
                .number(number)
                .street(street)
                .city(city)
                .state(state)
                .country(country)
                .zipCode(zipCode)
                .build());

        return uniqueId;
    }

    // checkers
    public void checkUserLoggedIn() throws IllegalStateException {
        if (this.loggedUser == null) {
            throw new IllegalStateException("You are not logged in");
        }
    }

    public void checkUserLoggedInAsCustomer() throws IllegalStateException {
        if (this.loggedUserType != UserType.CUSTOMER) {
            throw new LoggedUserTypeException(UserType.CUSTOMER);
        }
    }

    public void checkUserLoggedInAsLocalAdministrator() throws IllegalStateException {
        if (this.loggedUserType != UserType.LOCAL_ADMINISTRATOR) {
            throw new LoggedUserTypeException(UserType.LOCAL_ADMINISTRATOR);
        }
    }

    public void checkUserLoggedInAsDeliveryPerson() throws IllegalStateException {
        if (this.loggedUserType != UserType.DELIVERY_PERSON) {
            throw new LoggedUserTypeException(UserType.DELIVERY_PERSON);
        }
    }

    public void checkUserLoggedInAsCustomerOrLocalAdministrator() throws IllegalStateException {
        if (this.loggedUserType != UserType.CUSTOMER && this.loggedUserType != UserType.LOCAL_ADMINISTRATOR) {
            throw new LoggedUserTypeException(UserType.CUSTOMER, UserType.LOCAL_ADMINISTRATOR);
        }
    }
}
