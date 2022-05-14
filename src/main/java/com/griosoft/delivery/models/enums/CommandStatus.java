package main.java.com.griosoft.delivery.models.enums;

public enum CommandStatus {
    PENDING,
    DELIVERY_ACCEPTED,
    DELIVERED,
    CANCELLED,
    UNKNOWN;

    public static CommandStatus getCommandStatus(String status) {
        for (CommandStatus commandStatus : CommandStatus.values()) {
            if (commandStatus.name().equals(status.toUpperCase())) {
                return commandStatus;
            }
        }
        return CommandStatus.UNKNOWN;
    }

    public static void printCommandStatuses() {
        for (CommandStatus commandStatus : CommandStatus.values()) {
            System.out.print(commandStatus.name() + " ");
        }
        System.out.print("\n");
    }
}
