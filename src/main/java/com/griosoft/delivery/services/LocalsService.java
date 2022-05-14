package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Local;
import main.java.com.griosoft.delivery.repositories.csv.LocalCSV;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class LocalsService {
    LocalCSV repository;

    public LocalsService() {
        repository = LocalCSV.getInstance();
    }

    public List<Local> getLocals() {
        return repository.getLocals();
    }

    public Stream<Local> getLocalsStream() {
        return repository.getLocals().stream();
    }

    public int getLocalCount() {
        return repository.getLocals().size();
    }

    public Local getLocalById(String id) {
        return this.getLocalsStream().filter(local -> local.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Local> getLocalsByAdministratorId(String administratorId) {
        return this.getLocalsStream().filter(local -> local.getAdministratorId().equals(administratorId));
    }

    public void printLocals(int skip, int limit) {
        this.getLocalsStream().skip(skip).limit(limit).forEach(System.out::println);
    }

    public String getNewUUID() {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getLocalById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addLocal(Local local) {
        repository.create(local);
    }

    public void updateLocal(Local local) {
        repository.update(local);
    }

    public void deleteLocal(Local local) {
        repository.delete(local);
    }
}
