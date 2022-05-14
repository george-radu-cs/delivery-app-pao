package main.java.com.griosoft.delivery.models;

public final class Address {
    private String id;
    private Integer number;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public Address(String id, Integer number, String street, String city, String state, String country, String zipCode) {
        this.id = id;
        this.number = number;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }

    public Address(Builder builder) {
        this.id = builder.id;
        this.number = builder.number;
        this.street = builder.street;
        this.city = builder.city;
        this.state = builder.state;
        this.country = builder.country;
        this.zipCode = builder.zipCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", number=" + number +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }

    public String toCSV() {
        return id + "," + number + "," + street + "," + city + "," + state + "," + country + "," + zipCode;
    }

    public static class Builder {
        private String id;
        private Integer number;
        private String street;
        private String city;
        private String state;
        private String country;
        private String zipCode;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder number(Integer number) {
            this.number = number;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder zipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
