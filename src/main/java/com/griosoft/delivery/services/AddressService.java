package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Address;
import main.java.com.griosoft.delivery.repositories.AddressRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AddressService {
    //    private final AddressCSV repository;
    private final AddressRepository repository;

    public AddressService() {
//        repository = AddressCSV.getInstance();
        this.repository = new AddressRepository();
    }

    public List<Address> getAddresses() throws Exception {
        return repository.getAddresses();
    }

    public Stream<Address> getAddressesStream() throws Exception {
        return repository.getAddresses().stream();
    }

    public Address getAddressById(String id) throws Exception {
        return this.getAddressesStream().filter(address -> address.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Address> getAddressesByIds(List<String> ids) throws Exception {
        return this.getAddressesStream().filter(address -> ids.contains(address.getId()));
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getAddressById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void createAddress(Address address) throws Exception {
        repository.create(address);
    }

    public void updateAddress(Address address) throws Exception {
        repository.update(address);
    }

    public void deleteAddress(Address address) throws Exception {
        repository.delete(address);
    }
}
