package main.java.com.griosoft.delivery.models;

public class Product {
    protected String id;
    protected String localId;
    protected String name;
    protected String description;
    protected String price;
    protected String category;
    protected String quantity;
    protected String quantityMeasure;

    public Product(String id, String localId, String name, String description, String price, String category,
                   String quantity, String quantityMeasure) {
        this.id = id;
        this.localId = localId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.quantityMeasure = quantityMeasure;
    }

    public Product(Builder builder) {
        this.id = builder.id;
        this.localId = builder.localId;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.category = builder.category;
        this.quantity = builder.quantity;
        this.quantityMeasure = builder.quantityMeasure;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityMeasure() {
        return quantityMeasure;
    }

    public void setQuantityMeasure(String quantityMeasure) {
        this.quantityMeasure = quantityMeasure;
    }

    @Override
    public String toString() {
        return "Product{" + "id='" + id + '\'' + ", localId='" + localId + '\'' + ", name='" + name + '\'' + ", " +
                "description='" + description + '\'' + ", price='" + price + '\'' + ", category='" + category + '\'' +
                ", quantity='" + quantity + '\'' + ", quantityMeasure='" + quantityMeasure + '\'' + '}';
    }

    public String toCSV() {
        return id + "," + localId + "," + name + "," + description + "," + price + "," + category + "," + quantity +
                "," + quantityMeasure;
    }

    public static class Builder<T extends Builder> {
        protected String id;
        protected String localId;
        protected String name;
        protected String description;
        protected String price;
        protected String category;
        protected String quantity;
        protected String quantityMeasure;

        protected T self() {
            return (T) this;
        }

        public T id(String id) {
            this.id = id;
            return self();
        }

        public T localId(String localId) {
            this.localId = localId;
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        public T price(String price) {
            this.price = price;
            return self();
        }

        public T category(String category) {
            this.category = category;
            return self();
        }

        public T quantity(String quantity) {
            this.quantity = quantity;
            return self();
        }

        public T quantityMeasure(String quantityMeasure) {
            this.quantityMeasure = quantityMeasure;
            return self();
        }

        public Product build() {
            return new Product(this);
        }
    }
}
