package main.java.com.griosoft.delivery.exceptions;

public class UserIsNotCustomerException extends RuntimeException {
    public UserIsNotCustomerException(String message) {
        super(message);
    }
}
