package main.java.com.griosoft.delivery;

import main.java.com.griosoft.delivery.models.enums.UserCommand;
import main.java.com.griosoft.delivery.repositories.csv.*;
import main.java.com.griosoft.delivery.services.AuditService;
import main.java.com.griosoft.delivery.services.MainService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuditService auditService = AuditService.getInstance();
        MainService mainService = new MainService();

        // load values from csv
        AddressCSV.getInstance().loadFromCSV();
        CustomerAddressesCSV.getInstance().loadFromCSV();
        CommandCSV.getInstance().loadFromCSV();
        CommandProductCSV.getInstance().loadFromCSV();
        LocalCSV.getInstance().loadFromCSV();
        ProductCSV.getInstance().loadFromCSV();
        UserCSV.getInstance().loadFromCSV();

        Scanner scanner = new Scanner(System.in);
        boolean hasEnded = false;
        String commandString;
        UserCommand userCommand;

        while (!hasEnded) {
            try {
                System.out.println("> Enter command: (help for help)");
                commandString = scanner.nextLine().toUpperCase();
                userCommand = UserCommand.getCommand(commandString);
                auditService.log("User entered command: " + commandString);
                switch (userCommand) {
                    case LOGIN -> mainService.login(scanner);
                    case REGISTER -> mainService.register(scanner);
                    case LOGOUT -> mainService.logout();
                    case CHANGE_PASSWORD -> mainService.changePassword(scanner);
                    case EXIT -> {
                        try {
                            mainService.logout();
                        } catch (Exception e) {
                            // do nothing if user is not logged in
                        }
                        hasEnded = true;
                    }
                    case HELP -> mainService.printHelp();
                    case TEST -> mainService.test();
                    case SEE_LIST_LOCALS -> mainService.seeListOfLocals(scanner);
                    case SEE_LOCAL_INFO -> mainService.seeLocalInfo(scanner);
                    case CREATE_COMMAND -> mainService.createCommand(scanner);
                    case CANCEL_COMMAND -> mainService.cancelCommand(scanner);
                    case SEE_MY_COMMANDS -> mainService.seeMyCommands(scanner);
                    case SEE_MY_ADDRESSES -> mainService.seeMyAddresses(scanner);
                    case ADD_ADDRESS -> mainService.addAddress(scanner);
                    case CHANGE_CURRENT_ADDRESS -> mainService.changeCurrentAddress(scanner);
                    case SEE_COMMANDS_AVAILABLE_FOR_DELIVERY -> mainService.seeCommandsAvailableForDelivery(scanner);
                    case ACCEPT_COMMAND -> mainService.acceptCommand(scanner);
                    case DELIVER_COMMAND -> mainService.deliverCommand(scanner);
                    case ADD_LOCAL -> mainService.addLocal(scanner);
                    case UPDATE_LOCAL -> mainService.updateLocal(scanner);
                    case DELETE_LOCAL -> mainService.deleteLocal(scanner);
                    case SEE_MY_LOCALS -> mainService.seeMyLocals(scanner);
                    case SEE_LIST_PRODUCTS_FOR_LOCAL -> mainService.seeListOfProductsForLocal(scanner);
                    case ADD_PRODUCT_FOR_LOCAL -> mainService.addProductForLocal(scanner);
                    case SEE_COMMANDS_FOR_LOCAL -> mainService.seeCommandsForLocal(scanner);
                    case SEE_ACTIVE_COMMANDS_FOR_LOCAL -> mainService.seeActiveCommandsForLocal(scanner);
                    case UNKNOWN -> {
                        System.out.println("Unknown command");
                        throw new Exception("Unknown command");
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + userCommand);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                auditService.log("Error: " + e.getMessage());
            }
        }

        // saving to csv files
        AddressCSV.getInstance().saveToCSV();
        CustomerAddressesCSV.getInstance().saveToCSV();
        CommandCSV.getInstance().saveToCSV();
        CommandProductCSV.getInstance().saveToCSV();
        LocalCSV.getInstance().saveToCSV();
        ProductCSV.getInstance().saveToCSV();
        UserCSV.getInstance().saveToCSV();
    }
}
