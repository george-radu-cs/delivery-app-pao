package main.java.com.griosoft.delivery.models;

public class CommandProduct extends Product {
    private String commandId;
    private Integer numberOfProducts;

    public CommandProduct(String id, String localId, String name, String description, String price, String category,
                          String quantity, String quantityMeasure, String commandId, Integer numberOfProducts) {
        super(id, localId, name, description, price, category, quantity, quantityMeasure);
        this.commandId = commandId;
        this.numberOfProducts = numberOfProducts;
    }

    public CommandProduct(Builder builder) {
        super(builder);
        this.commandId = builder.commandId;
        this.numberOfProducts = builder.numberOfProducts;
    }

    @Override
    public String toString() {
        return "CommandProduct{" + "commandId='" + commandId + '\'' + ", numberOfProducts=" + numberOfProducts + ", " +
                "id='" + id + '\'' + ", localId='" + localId + '\'' + ", name='" + name + '\'' + ", description='" +
                description + '\'' + ", price='" + price + '\'' + ", category='" + category + '\'' + ", quantity='" +
                quantity + '\'' + ", quantityMeasure='" + quantityMeasure + '\'' + '}';
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + commandId + "," + numberOfProducts;
    }

    public static class Builder extends Product.Builder<Builder> {
        private String commandId;
        private Integer numberOfProducts;

        public Builder() {
            super();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder commandId(String commandId) {
            this.commandId = commandId;
            return this;
        }

        public Builder numberOfProducts(Integer numberOfProducts) {
            this.numberOfProducts = numberOfProducts;
            return this;
        }

        @Override
        public CommandProduct build() {
            return new CommandProduct(this);
        }
    }
}
