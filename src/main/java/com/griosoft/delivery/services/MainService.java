package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.exceptions.LoggedUserTypeException;
import main.java.com.griosoft.delivery.models.*;
import main.java.com.griosoft.delivery.models.enums.CommandStatus;
import main.java.com.griosoft.delivery.models.enums.TransportType;
import main.java.com.griosoft.delivery.models.enums.UserType;
import main.java.com.griosoft.delivery.utils.UserCommandsDescriptors;

import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainService {
    private static final Integer MAX_ENTRIES = 10;

    private final UserService userService;
    private final AddressService addressService;
    private final CustomerAddressesService customerAddressesService;
    private final CommandService commandService;
    private final CommandProductsService commandProductsService;
    private final LocalsService localsService;
    private final ProductService productService;

    private User loggedUser = null;
    private UserType loggedUserType = null;

    public MainService() {
        this.userService = new UserService();
        this.addressService = new AddressService();
        this.customerAddressesService = new CustomerAddressesService();
        this.commandService = new CommandService();
        this.commandProductsService = new CommandProductsService();
        this.localsService = new LocalsService();
        this.productService = new ProductService();
    }

    public void test() throws Exception {
        System.out.println(loggedUserType);
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
        String uniqueId = this.userService.getNewUUID();
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
        this.userService.addUser(user, userType);
    }

    public void login(Scanner scanner) throws Exception {
        if (this.loggedUser != null) {
            throw new IllegalStateException("You are already logged in");
        }
        System.out.println("Enter your email");
        String email = scanner.nextLine();
        System.out.println("Enter your password");
        String password = scanner.nextLine();
        User user = this.userService.getUserByEmail(email);
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
        this.userService.updateUserPassword(this.loggedUser.getId(), newPassword, this.getLoggedUserType());
    }

    public void seeMyInfo() throws Exception {
        this.checkUserLoggedIn();
        System.out.println(this.loggedUser);
    }

    public void updateMyInfo(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        System.out.println("Enter your new email");
        String newEmail = scanner.nextLine();
        System.out.println("Enter your new first name");
        String newFirstName = scanner.nextLine();
        System.out.println("Enter your new last name");
        String newLastName = scanner.nextLine();
        System.out.println("Enter your new phone number");
        String newPhoneNumber = scanner.nextLine();
        User.Builder userBuilder = null;
        switch (this.getLoggedUserType()) {
            case CUSTOMER -> {
                userBuilder = new Customer.Builder();
                System.out.println("Enter your new address id");
                String newAddressId = scanner.nextLine();
                ((Customer.Builder) userBuilder).currentAddressId(newAddressId);
            }
            case DELIVERY_PERSON -> {
                userBuilder = new DeliveryPerson.Builder();
                System.out.println("Enter your new transport type");
                TransportType newTransportType = TransportType.getTransportType(scanner.nextLine());
                ((DeliveryPerson.Builder) userBuilder).transportType(newTransportType);
            }
            case LOCAL_ADMINISTRATOR -> {
                userBuilder = new LocalAdministrator.Builder();
                System.out.println("Enter your new administrator certificate number");
                String newAdministratorCertificate = scanner.nextLine();
                ((LocalAdministrator.Builder) userBuilder).administratorCertificate(newAdministratorCertificate);
            }
            case UNKNOWN -> throw new Exception("Unknown user type");
        }
        User updatedUser = userBuilder
                .id(this.loggedUser.getId())
                .firstName(newFirstName)
                .lastName(newLastName)
                .email(newEmail)
                .phoneNumber(newPhoneNumber)
                .password(this.loggedUser.getPassword())
                .build();
        this.userService.updateUser(updatedUser, this.loggedUserType);
        this.setLoggedUser(updatedUser);
    }

    public void deleteAccount(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        System.out.println("Are you sure you want to delete your account? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            this.userService.deleteUser(this.loggedUser);
            this.logout();
        }
    }

    // customer commands
    public void seeListOfLocals(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();
        this.localsService.printLocals(0, MAX_ENTRIES);
        int numberOfLocalsPrinted = MAX_ENTRIES;
        int numberOfLocals = this.localsService.getLocalCount();
        while (numberOfLocalsPrinted < numberOfLocals) {
            System.out.println("...more locals...");
            System.out.println("Do you want to see more locals? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                this.localsService.printLocals(numberOfLocalsPrinted, MAX_ENTRIES);
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
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        if (this.getLoggedUserType() == UserType.LOCAL_ADMINISTRATOR &&
                !local.getAdministratorId().equals(this.loggedUser.getId())) {
            throw new IllegalStateException("You are not the administrator of this local");
        }
        System.out.println(local);
        System.out.println("Products:");
        this.productService.getProductsByLocalId(localId).forEach(System.out::println);
    }

    public void createCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the local you want to order from");
        String localId = scanner.nextLine();
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }

        String uniqueId = this.commandService.getNewUUID();

        List<Product> localProducts = this.productService.getProductsByLocalId(localId).toList();
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
            String commandProductId = this.commandService.getNewUUID();
            commandProducts.add(new CommandProduct.Builder()
                    .id(commandProductId)
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

        this.commandService.addCommand(new Command.Builder()
                .id(uniqueId)
                .customerId(loggedUser.getId())
                .localId(localId)
                .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
                .deliveredAt(null)
                .status(CommandStatus.PENDING)
                .build());
        this.commandProductsService.addCommandProducts(commandProducts);
        System.out.println("Command created");
    }

    public void cancelCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the command you want to cancel");
        String commandId = scanner.nextLine();
        Command command = this.commandService.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getCustomerId().equals(this.loggedUser.getId())) {
            if (command.getStatus() == CommandStatus.PENDING) {
                this.commandService.updateCommandStatus(commandId, CommandStatus.CANCELLED);
            }
        }
        System.out.println("Command cancelled");
    }

    public void deleteCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the command you want to delete");
        String commandId = scanner.nextLine();
        Command command = this.commandService.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getCustomerId().equals(this.loggedUser.getId())) {
            if (command.getStatus() == CommandStatus.PENDING || command.getStatus() == CommandStatus.CANCELLED) {
                for (CommandProduct commandProduct : this.commandProductsService.getCommandProductsByCommandId(commandId)) {
                    this.commandProductsService.deleteCommandProduct(commandProduct);
                }
                this.commandService.deleteCommand(command);
                System.out.println("Command deleted");
            }
        }
    }

    public void seeMyCommands(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Your commands:");
        this.commandService.getCommandsByCustomerId(this.loggedUser.getId()).limit(MAX_ENTRIES)
                           .forEach(System.out::println);
        var numberOfCommands = this.commandService.getCommandsByCustomerId(this.loggedUser.getId()).count();
        int numberOfCommandsPrinted = MAX_ENTRIES;
        while (numberOfCommandsPrinted < numberOfCommands) {
            System.out.println("...more commands...");
            System.out.println("Do you want to see more commands? (y/n)");
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                this.commandService.getCommandsByCustomerId(this.loggedUser.getId()).skip(numberOfCommandsPrinted)
                                   .limit(MAX_ENTRIES)
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
        if (!this.customerAddressesService.checkIfCustomerHasAddresses(this.loggedUser.getId())) {
            this.customerAddressesService.createCustomerAddressesList(this.loggedUser.getId());
            this.userService.updateCustomerCurrentAddressId(this.loggedUser.getId(), addressId);
        }
        this.customerAddressesService.addAddressToCustomer(this.loggedUser.getId(), addressId);
    }

    public void updateAddress(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the address you want to update");
        String addressId = scanner.nextLine();
        Address address = this.addressService.getAddressById(addressId);
        if (address == null) {
            throw new Exception("Address with id " + addressId + " not found");
        }
        if (!this.customerAddressesService.checkIfCustomerHasAddress(this.loggedUser.getId(), addressId)) {
            throw new Exception("Address with id " + addressId + " not found");
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
        this.addressService.updateAddress(new Address.Builder()
                .id(addressId)
                .number(number)
                .street(street)
                .city(city)
                .state(state)
                .country(country)
                .zipCode(zipCode)
                .build());
    }

    public void deleteAddress(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the address you want to delete");
        String addressId = scanner.nextLine();
        Address address = this.addressService.getAddressById(addressId);
        if (address == null) {
            throw new Exception("Address with id " + addressId + " not found");
        }
        if (this.customerAddressesService.checkIfCustomerHasAddress(this.loggedUser.getId(), addressId)) {
            this.customerAddressesService.deleteAddressFromCustomer(this.loggedUser.getId(), addressId);
            if (((Customer) this.loggedUser).getCurrentAddressId().equals(addressId)) {
                this.userService.updateCustomerCurrentAddressId(this.loggedUser.getId(), null);
                this.loggedUser = this.userService.getUserById(this.loggedUser.getId());
            }
            this.addressService.deleteAddress(address);
        }
    }

    public void changeCurrentAddress(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsCustomer();

        System.out.println("Enter the id of the address you want to use as your current address");
        String addressId = scanner.nextLine();
        if (!this.customerAddressesService.checkIfCustomerHasAddress(this.loggedUser.getId(), addressId)) {
            throw new Exception("Address with id " + addressId + " not found");
        }
        this.userService.updateCustomerCurrentAddressId(this.loggedUser.getId(), addressId);
        this.loggedUser = this.userService.getUserById(this.loggedUser.getId());
    }

    // delivery person commands
    public void seeCommandsAvailableForDelivery(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Commands available for delivery:");
        this.commandService.getActiveCommands().forEach(System.out::println);
    }

    public void acceptCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Enter the id of the command you want to accept");
        String commandId = scanner.nextLine();
        Command command = this.commandService.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getStatus() == CommandStatus.PENDING) {
            this.commandService.updateCommandStatus(commandId, CommandStatus.DELIVERY_ACCEPTED);
        }
    }

    public void deliverCommand(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsDeliveryPerson();

        System.out.println("Enter the id of the command you want to deliver");
        String commandId = scanner.nextLine();
        Command command = this.commandService.getCommandById(commandId);
        if (command == null) {
            throw new Exception("Command with id " + commandId + " not found");
        }
        if (command.getStatus() == CommandStatus.DELIVERY_ACCEPTED) {
            this.commandService.updateCommandStatus(commandId, CommandStatus.DELIVERED);
        }
    }

    // local administrator commands
    public void addLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        String uniqueId = this.localsService.getNewUUID();
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

        this.localsService.addLocal(new Local.Builder()
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
        Local local = this.localsService.getLocalById(localId);
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

        this.localsService.updateLocal(new Local.Builder()
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
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.localsService.deleteLocal(local);
        System.out.println("Local deleted");
    }

    public void seeMyLocals(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        var userLocals = this.localsService.getLocalsByAdministratorId(this.loggedUser.getId()).toList();
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
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.productService.getProductsByLocalId(localId).forEach(System.out::println);
    }

    public void addProductForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        String uniqueId = this.productService.getNewUUID();
        System.out.println("Enter the id of the local you want to add the product to");
        String localId = scanner.nextLine();
        Local local = this.localsService.getLocalById(localId);
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
        this.productService.addProduct(new Product.Builder()
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

    public void updateProductForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the product you want to update");
        String productId = scanner.nextLine();
        Product product = this.productService.getProductById(productId);
        if (product == null) {
            throw new Exception("Product with id " + productId + " not found");
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
        this.productService.updateProduct(new Product.Builder()
                .id(productId)
                .localId(product.getLocalId())
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .quantity(quantity)
                .quantityMeasure(quantityMeasure)
                .build());
    }

    public void deleteProductForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to remove a product from");
        String localId = scanner.nextLine();
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        System.out.println("Enter the id of the product you want to delete");
        this.productService.getProductsByLocalId(localId).forEach(System.out::println);
        System.out.println("Enter the id of the product you want to delete");
        String productId = scanner.nextLine();
        Product product = this.productService.getProductById(productId);
        if (product == null) {
            throw new Exception("Product with id " + productId + " not found");
        }
        this.productService.deleteProduct(product);
    }

    public void seeCommandsForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to see the commands of");
        String localId = scanner.nextLine();
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.commandService.getCommandsByLocalId(localId).forEach(System.out::println);
    }

    public void seeActiveCommandsForLocal(Scanner scanner) throws Exception {
        this.checkUserLoggedIn();
        this.checkUserLoggedInAsLocalAdministrator();

        System.out.println("Enter the id of the local you want to see the active commands of");
        String localId = scanner.nextLine();
        Local local = this.localsService.getLocalById(localId);
        if (local == null) {
            throw new Exception("Local with id " + localId + " not found");
        }
        this.commandService.printActiveCommandsForLocal(localId);
    }

    public String createAddress(Scanner scanner) throws Exception {
        String uniqueId = this.addressService.getNewUUID();
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
        this.addressService.createAddress(new Address.Builder()
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

    public Stream<Address> getAddressesByCustomerId(String customerId) throws Exception {
        var customerAddressesIds = this.customerAddressesService.getCustomerAddressesIds().get(customerId);
        return this.addressService.getAddressesByIds(customerAddressesIds);
    }

    // checkers
    public void checkUserLoggedIn() throws IllegalStateException {
        if (this.loggedUser == null) {
            throw new IllegalStateException("You are not logged in");
        }
    }

    public void checkUserLoggedInAsCustomer() throws IllegalStateException {
        if (this.getLoggedUserType() != UserType.CUSTOMER) {
            throw new LoggedUserTypeException(UserType.CUSTOMER);
        }
    }

    public void checkUserLoggedInAsLocalAdministrator() throws IllegalStateException {
        if (this.getLoggedUserType() != UserType.LOCAL_ADMINISTRATOR) {
            throw new LoggedUserTypeException(UserType.LOCAL_ADMINISTRATOR);
        }
    }

    public void checkUserLoggedInAsDeliveryPerson() throws IllegalStateException {
        if (this.getLoggedUserType() != UserType.DELIVERY_PERSON) {
            throw new LoggedUserTypeException(UserType.DELIVERY_PERSON);
        }
    }

    public void checkUserLoggedInAsCustomerOrLocalAdministrator() throws IllegalStateException {
        if (this.getLoggedUserType() != UserType.CUSTOMER && this.getLoggedUserType() != UserType.LOCAL_ADMINISTRATOR) {
            throw new LoggedUserTypeException(UserType.CUSTOMER, UserType.LOCAL_ADMINISTRATOR);
        }
    }
}
