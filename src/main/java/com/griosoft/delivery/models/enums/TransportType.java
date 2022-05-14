package main.java.com.griosoft.delivery.models.enums;

public enum TransportType {
    CAR,
    BIKE,
    UNKNOWN;

    public static TransportType getTransportType(String type) {
        for (TransportType transportType : TransportType.values()) {
            if (transportType.name().equals(type.toUpperCase())) {
                return transportType;
            }
        }
        return TransportType.UNKNOWN;
    }

    public static void printTransportTypes() {
        for (TransportType transportType : TransportType.values()) {
            System.out.print(transportType.name() + " ");
        }
        System.out.print("\n");
    }
}
