package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Address;
import main.java.com.griosoft.delivery.repositories.csv.AddressCSV;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AddressService {
    private final AddressCSV repository;

    public AddressService() {
        repository = AddressCSV.getInstance();
    }

    public List<Address> getAddresses() {
        return repository.getAddresses();
    }

    public Stream<Address> getAddressesStream() {
        return repository.getAddresses().stream();
    }

    public Address getAddressById(String id) {
        return this.getAddressesStream().filter(address -> address.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Address> getAddressesByIds(List<String> ids) {
        return this.getAddressesStream().filter(address -> ids.contains(address.getId()));
    }

    public String getNewUUID() {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getAddressById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void createAddress(Address address) {
        repository.create(address);
    }

    public void updateAddress(Address address) {
        repository.update(address);
    }

    public void deleteAddress(Address address) {
        repository.delete(address);
    }
}
