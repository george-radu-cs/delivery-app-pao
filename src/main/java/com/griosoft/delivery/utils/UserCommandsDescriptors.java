package main.java.com.griosoft.delivery.utils;

import main.java.com.griosoft.delivery.models.enums.UserCommand;
import main.java.com.griosoft.delivery.models.enums.UserType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserCommandsDescriptors {
    private static UserCommandsDescriptors instance = null;
    private final LinkedHashMap<UserCommand, String> userNotConnectedCommands = new LinkedHashMap<>();
    private final LinkedHashMap<UserCommand, String> userConnectedCommands = new LinkedHashMap<>();
    private final Map<UserType, LinkedHashMap<UserCommand, String>> userCommandsDetails = new HashMap<>();

    private UserCommandsDescriptors() {
        this.userNotConnectedCommands.put(UserCommand.LOGIN, "login to the system");
        this.userNotConnectedCommands.put(UserCommand.REGISTER, "sign up to the system");
        this.userNotConnectedCommands.put(UserCommand.EXIT, "exit the system");
        this.userNotConnectedCommands.put(UserCommand.HELP, "show help");

        this.userConnectedCommands.put(UserCommand.LOGOUT, "logout from the system");
        this.userConnectedCommands.put(UserCommand.CHANGE_PASSWORD, "change password for your account");
        this.userConnectedCommands.put(UserCommand.HELP, "show help");

        LinkedHashMap<UserCommand, String> customerCommands = new LinkedHashMap<>();
        customerCommands.put(UserCommand.SEE_LIST_LOCALS, "see the list of available locals to order");
        customerCommands.put(UserCommand.SEE_LOCAL_INFO, "see info about a local and its products");
        customerCommands.put(UserCommand.CREATE_COMMAND, "create a command from a specific local");
        customerCommands.put(UserCommand.CANCEL_COMMAND, "cancel a command");
        customerCommands.put(UserCommand.SEE_MY_COMMANDS, "see the list of your commands");
        customerCommands.put(UserCommand.SEE_MY_ADDRESSES, "see the list of your addresses saved in the app");
        customerCommands.put(UserCommand.ADD_ADDRESS, "add an address for delivery to your account");
        customerCommands.put(UserCommand.CHANGE_CURRENT_ADDRESS, "see the list of all commands ordered by you");
        this.userCommandsDetails.put(UserType.CUSTOMER, customerCommands);

        LinkedHashMap<UserCommand, String> deliveryPersonCommands = new LinkedHashMap<>();
        deliveryPersonCommands.put(UserCommand.SEE_COMMANDS_AVAILABLE_FOR_DELIVERY, "see the list of all commands available for delivery");
        deliveryPersonCommands.put(UserCommand.ACCEPT_COMMAND, "accept a command for delivery");
        deliveryPersonCommands.put(UserCommand.DELIVER_COMMAND, "deliver a command");
        this.userCommandsDetails.put(UserType.DELIVERY_PERSON, deliveryPersonCommands);

        LinkedHashMap<UserCommand, String> localAdministratorCommands = new LinkedHashMap<>();
        localAdministratorCommands.put(UserCommand.ADD_LOCAL, "add a local to the system");
        localAdministratorCommands.put(UserCommand.UPDATE_LOCAL, "update a local in the system");
        localAdministratorCommands.put(UserCommand.DELETE_LOCAL, "delete a local from the system");
        localAdministratorCommands.put(UserCommand.SEE_MY_LOCALS, "see the list of your locals");
        localAdministratorCommands.put(UserCommand.SEE_LIST_PRODUCTS_FOR_LOCAL, "see the list of products for a specific local");
        localAdministratorCommands.put(UserCommand.ADD_PRODUCT_FOR_LOCAL, "add a product to a specific local");
        localAdministratorCommands.put(UserCommand.SEE_COMMANDS_FOR_LOCAL, "see the list of all commands for a specific local");
        localAdministratorCommands.put(UserCommand.SEE_ACTIVE_COMMANDS_FOR_LOCAL, "see the list of all active commands for a specific local");
        this.userCommandsDetails.put(UserType.LOCAL_ADMINISTRATOR, localAdministratorCommands);
    }

    public static UserCommandsDescriptors getInstance() {
        if (instance == null) {
            instance = new UserCommandsDescriptors();
        }
        return instance;
    }

    public LinkedHashMap<UserCommand, String> getUserNotConnectedCommands() {
        return userNotConnectedCommands;
    }

    public LinkedHashMap<UserCommand, String> getUserConnectedCommands() {
        return userConnectedCommands;
    }

    public Map<UserType, LinkedHashMap<UserCommand, String>> getUserCommandsDetails() {
        return userCommandsDetails;
    }
}
