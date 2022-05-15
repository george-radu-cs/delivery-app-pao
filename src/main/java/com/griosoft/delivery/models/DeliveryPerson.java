package main.java.com.griosoft.delivery.models;

import main.java.com.griosoft.delivery.models.enums.TransportType;

public class DeliveryPerson extends User {
    private TransportType transportType;

    public DeliveryPerson(String id, String firstName, String lastName, String email, String phone, String password,
                          TransportType transportType) {
        super(id, firstName, lastName, email, phone, password);
        this.transportType = transportType;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    @Override
    public String toString() {
        return "DeliveryPerson{" +
                "transportType=" + transportType +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public String toCSV() {
        return super.toCSV("DELIVERY_PERSON") + "," + transportType.toString();
    }

    public static class Builder extends User.Builder {
        private TransportType transportType;

        public Builder() {
        }

        public Builder transportType(TransportType transportType) {
            this.transportType = transportType;
            return this;
        }

        @Override
        public DeliveryPerson build() {
            return new DeliveryPerson(id, firstName, lastName, email, phoneNumber, password, transportType);
        }
    }
}
