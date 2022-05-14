package main.java.com.griosoft.delivery.models;

public class Customer extends User {
    private String currentAddressId;

    public Customer(String id, String firstName, String lastName, String email, String phone, String password) {
        super(id, firstName, lastName, email, phone, password);
    }

    public Customer(String id, String firstName, String lastName, String email, String phone, String password,
                    String addressId) {
        super(id, firstName, lastName, email, phone, password);
        this.currentAddressId = addressId;
    }

    public String getCurrentAddressId() {
        return currentAddressId;
    }

    public void setCurrentAddressId(String currentAddressId) {
        this.currentAddressId = currentAddressId;
    }

    @Override
    public String toString() {
        return "Customer{" + "id='" + id + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName +
                '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", password='" + password +
                '\'' + '}';
    }

    @Override
    public String toCSV() {
        return super.toCSV("CUSTOMER") + "," + currentAddressId;
    }

    public static class Builder extends User.Builder {
        private String currentAddressId;

        public Builder() {
        }

        public Builder currentAddressId(String currentAddressId) {
            this.currentAddressId = currentAddressId;
            return this;
        }

        @Override
        public Customer build() {
            return new Customer(id, firstName, lastName, email, phoneNumber, password, currentAddressId);
        }
    }
}
