package main.java.com.griosoft.delivery.models;

import main.java.com.griosoft.delivery.models.enums.CommandStatus;

import java.time.OffsetDateTime;
import java.util.Objects;

public final class Command {
    private String id;
    private String customerId;
    private String localId;
    private OffsetDateTime createdAt;
    private OffsetDateTime deliveredAt;
    private CommandStatus status; // pending, delivered, cancelled

    public Command(String id, String customerId, String localId, OffsetDateTime createdAt, OffsetDateTime deliveredAt,
                   CommandStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.localId = localId;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.status = status;
    }

    public Command(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.localId = builder.localId;
        this.createdAt = builder.createdAt;
        this.deliveredAt = builder.deliveredAt;
        this.status = builder.status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(OffsetDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return id.equals(command.id) && customerId.equals(command.customerId) && localId.equals(command.localId) &&
                createdAt.equals(command.createdAt) && deliveredAt.equals(command.deliveredAt) &&
                status.equals(command.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, localId, createdAt, deliveredAt, status);
    }

    @Override
    public String toString() {
        return "Command{" + "id='" + id + '\'' + ", customerId='" + customerId + '\'' + ", localId='" + localId + '\'' +
                ", createdAt=" + createdAt + ", deliveredAt=" + deliveredAt + ", status='" + status + '\'' + '}';
    }

    public String toCSV() {
        return id + "," + customerId + "," + localId + "," + createdAt + "," + deliveredAt + "," + status;
    }

    public static class Builder {
        private String id;
        private String customerId;
        private String localId;
        private OffsetDateTime createdAt;
        private OffsetDateTime deliveredAt;
        private CommandStatus status; // pending, delivered, cancelled

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder customerId(String userId) {
            this.customerId = userId;
            return this;
        }

        public Builder localId(String localId) {
            this.localId = localId;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder deliveredAt(OffsetDateTime deliveredAt) {
            this.deliveredAt = deliveredAt;
            return this;
        }

        public Builder status(CommandStatus status) {
            this.status = status;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
