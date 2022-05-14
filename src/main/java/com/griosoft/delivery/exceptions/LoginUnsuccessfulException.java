package main.java.com.griosoft.delivery.exceptions;

public class LoginUnsuccessfulException extends RuntimeException {
    public LoginUnsuccessfulException() {
        super("Login unsuccessful - verify your credentials");
    }
}
