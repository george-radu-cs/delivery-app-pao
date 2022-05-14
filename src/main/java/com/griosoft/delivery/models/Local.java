package main.java.com.griosoft.delivery.models;

import java.time.OffsetTime;

public final class Local {
    private String id;
    private String administratorId;
    private String name;
    private String description;
    private String addressId;
    private String phoneNumber;
    private String email;
    private String type;
    private String category;
    private OffsetTime openHour;
    private OffsetTime closeHour;
    private String status; // ACTIVE or INACTIVE

    public Local(String id, String administratorId, String name, String description, String addressId, String phoneNumber,
                 String email, String type, String category, OffsetTime openHour, OffsetTime closeHour,
                 String status) {
        this.id = id;
        this.administratorId = administratorId;
        this.name = name;
        this.description = description;
        this.addressId = addressId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.type = type;
        this.category = category;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.status = status;
    }

    public Local(Builder builder) {
        this.id = builder.id;
        this.administratorId = builder.administratorId;
        this.name = builder.name;
        this.description = builder.description;
        this.addressId = builder.addressId;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.type = builder.type;
        this.category = builder.category;
        this.openHour = builder.openHour;
        this.closeHour = builder.closeHour;
        this.status = builder.status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(String administratorId) {
        this.administratorId = administratorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public OffsetTime getOpenHour() {
        return openHour;
    }

    public void setOpenHour(OffsetTime openHour) {
        this.openHour = openHour;
    }

    public OffsetTime getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(OffsetTime closeHour) {
        this.closeHour = closeHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Local{" +
                "id='" + id + '\'' +
                ", administratorId='" + administratorId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", addressId='" + addressId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", openHour=" + openHour +
                ", closeHour=" + closeHour +
                ", status='" + status + '\'' +
                '}';
    }

    public String toCSV() {
        return id + "," + administratorId + "," + name + "," + description + "," + addressId + "," + phoneNumber + "," +
                email + "," + openHour + "," + closeHour + "," + type + "," + category + "," + status;
    }

    public static class Builder {
        private String id;
        private String administratorId;
        private String name;
        private String description;
        private String addressId;
        private String phoneNumber;
        private String email;
        private String type;
        private String category;
        private OffsetTime openHour;
        private OffsetTime closeHour;
        private String status; // ACTIVE or INACTIVE

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder administratorId(String administratorId) {
            this.administratorId = administratorId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder addressId(String addressId) {
            this.addressId = addressId;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder openHour(OffsetTime openHour) {
            this.openHour = openHour;
            return this;
        }

        public Builder closeHour(OffsetTime closeHour) {
            this.closeHour = closeHour;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Local build() {
            return new Local(this);
        }
    }
}
