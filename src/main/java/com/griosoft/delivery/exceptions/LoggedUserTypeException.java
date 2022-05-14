package main.java.com.griosoft.delivery.exceptions;

import main.java.com.griosoft.delivery.models.enums.UserType;

public class LoggedUserTypeException extends IllegalStateException {
    public LoggedUserTypeException(UserType userType) {
        super("Logged user is not a " + userType.name());
    }

    public LoggedUserTypeException(UserType userType1, UserType userType2) {
        super("Logged user is not a " + userType1.name() + " or " + userType2.name());
    }

    public LoggedUserTypeException(UserType userType1, UserType userType2, UserType userType3) {
        super("Logged user is not a " + userType1.name() + ", " + userType2.name() + " or " + userType3.name());
    }
}
