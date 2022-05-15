package main.java.com.griosoft.delivery.models.enums;

public enum UserCommand {
    LOGIN,
    REGISTER,
    LOGOUT,
    CHANGE_PASSWORD,
    SEE_MY_INFO,
    UPDATE_MY_INFO,
    DELETE_ACCOUNT,
    EXIT,
    HELP,
    TEST,
    // for customer
    SEE_LIST_LOCALS,
    SEE_LOCAL_INFO,
    CREATE_COMMAND,
    CANCEL_COMMAND,
    DELETE_COMMAND,
    SEE_MY_COMMANDS,
    ADD_ADDRESS,
    UPDATE_ADDRESS,
    DELETE_ADDRESS,
    SEE_MY_ADDRESSES,
    CHANGE_CURRENT_ADDRESS,
    // for delivery person
    SEE_COMMANDS_AVAILABLE_FOR_DELIVERY,
    ACCEPT_COMMAND,
    DELIVER_COMMAND,
    // for local administrator
    ADD_LOCAL,
    UPDATE_LOCAL,
    DELETE_LOCAL,
    SEE_MY_LOCALS,
    ADD_PRODUCT_FOR_LOCAL,
    UPDATE_PRODUCT_FOR_LOCAL,
    DELETE_PRODUCT_FOR_LOCAL,
    SEE_COMMANDS_FOR_LOCAL,
    SEE_ACTIVE_COMMANDS_FOR_LOCAL,
    SEE_LIST_PRODUCTS_FOR_LOCAL,
    UNKNOWN;

    public static UserCommand getCommand(String commandString) {
        for (UserCommand command : UserCommand.values()) {
            if (command.name().equals(commandString.toUpperCase()) || command
                    .name()
                    .equals(commandString.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase())) {
                return command;
            }
        }
        return UNKNOWN;
    }
}
