package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.repositories.CustomerAddressRepository;

import java.util.List;
import java.util.Map;

public class CustomerAddressesService {
    //    private final CustomerAddressesCSV repository;
    private final CustomerAddressRepository repository;

    public CustomerAddressesService() {
//        repository = CustomerAddressesCSV.getInstance();
        this.repository = new CustomerAddressRepository();
    }

    public Map<String, List<String>> getCustomerAddressesIds() throws Exception {
        return repository.getCustomerAddressesIds();
    }

    public boolean checkIfCustomerHasAddresses(String customerId) throws Exception {
        return repository.getCustomerAddressesIds().containsKey(customerId);
    }

    public void createCustomerAddressesList(String customerId) throws Exception {
        this.repository.createCustomerAddressesList(customerId);
    }

    public void addAddressToCustomer(String customerId, String addressId) throws Exception {
        this.repository.addAddressToCustomer(customerId, addressId);
    }

    public void deleteAddressFromCustomer(String customerId, String addressId) throws Exception {
        this.repository.deleteAddressFromCustomer(customerId, addressId);
    }

    public boolean checkIfCustomerHasAddress(String customerId, String addressId) throws Exception {
        return repository.getCustomerAddressesIds().get(customerId).contains(addressId);
    }
}
