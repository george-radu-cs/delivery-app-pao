package main.java.com.griosoft.delivery.models.enums;

public enum UserType {
    CUSTOMER,
    DELIVERY_PERSON,
    LOCAL_ADMINISTRATOR,
    UNKNOWN;

    public static UserType getUserType(String userType) {
        for (UserType type : UserType.values()) {
            if (type.name().equals(userType.toUpperCase()) || type
                    .name()
                    .equals(userType.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static void printUserTypes() {
        for (UserType type : UserType.values()) {
            System.out.println(type.name());
        }
    }
}