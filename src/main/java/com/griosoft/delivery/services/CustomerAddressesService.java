package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.repositories.csv.CustomerAddressesCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerAddressesService {
    private final CustomerAddressesCSV repository;

    public CustomerAddressesService() {
        repository = CustomerAddressesCSV.getInstance();
    }

    public Map<String, List<String>> getCustomerAddressesIds() {
        return repository.getCustomerAddressesIds();
    }

    public boolean checkIfCustomerHasAddresses(String customerId) {
        return repository.getCustomerAddressesIds().containsKey(customerId);
    }

    public void createCustomerAddressesList(String customerId) {
        repository.getCustomerAddressesIds().put(customerId, new ArrayList<>());
    }

    public void addAddressToCustomer(String customerId, String addressId) {
        repository.getCustomerAddressesIds().get(customerId).add(addressId);
    }

    public boolean checkIfCustomerHasAddress(String customerId, String addressId) {
        return repository.getCustomerAddressesIds().get(customerId).contains(addressId);
    }
}
