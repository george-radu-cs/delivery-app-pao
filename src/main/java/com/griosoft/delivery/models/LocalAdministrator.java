package main.java.com.griosoft.delivery.models;

public class LocalAdministrator extends User {
    private String administratorCertificate;

    public LocalAdministrator(String id, String firstName, String lastName, String email, String phone,
                              String password, String administratorCertificate) {
        super(id, firstName, lastName, email, phone, password);
        this.administratorCertificate = administratorCertificate;
    }

    @Override
    public String toString() {
        return "LocalAdministrator{" + "administratorCertificate='" + administratorCertificate + '\'' + ", id='" + id +
                '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email +
                '\'' + ", phone='" + phoneNumber + '\'' + ", password='" + password + '\'' + '}';
    }

    @Override
    public String toCSV() {
        return super.toCSV("LOCAL_ADMINISTRATOR") + "," + administratorCertificate;
    }

    public static class Builder extends User.Builder {
        private String administratorCertificate;

        public Builder() {
        }

        public Builder administratorCertificate(String administratorCertificate) {
            this.administratorCertificate = administratorCertificate;
            return this;
        }

        @Override
        public LocalAdministrator build() {
            return new LocalAdministrator(id, firstName, lastName, email, phoneNumber, password,
                    administratorCertificate);
        }
    }
}
