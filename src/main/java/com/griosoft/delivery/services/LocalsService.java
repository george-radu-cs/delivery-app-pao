package main.java.com.griosoft.delivery.services;

import main.java.com.griosoft.delivery.models.Local;
import main.java.com.griosoft.delivery.repositories.LocalRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class LocalsService {
    //    private final LocalCSV repository;
    private final LocalRepository repository;

    public LocalsService() {
//        repository = LocalCSV.getInstance();
        this.repository = new LocalRepository();
    }

    public List<Local> getLocals() throws Exception {
        return repository.getLocals();
    }

    public Stream<Local> getLocalsStream() throws Exception {
        return repository.getLocals().stream();
    }

    public int getLocalCount() throws Exception {
        return repository.getLocals().size();
    }

    public Local getLocalById(String id) throws Exception {
        return this.getLocalsStream().filter(local -> local.getId().equals(id)).findFirst().orElse(null);
    }

    public Stream<Local> getLocalsByAdministratorId(String administratorId) throws Exception {
        return this.getLocalsStream().filter(local -> local.getAdministratorId().equals(administratorId));
    }

    public void printLocals(int skip, int limit) throws Exception {
        this.getLocalsStream().skip(skip).limit(limit).forEach(System.out::println);
    }

    public String getNewUUID() throws Exception {
        String uniqueId = UUID.randomUUID().toString();
        while (this.getLocalById(uniqueId) != null) {
            uniqueId = UUID.randomUUID().toString();
        }
        return uniqueId;
    }

    public void addLocal(Local local) throws Exception {
        repository.create(local);
    }

    public void updateLocal(Local local) throws Exception {
        repository.update(local);
    }

    public void deleteLocal(Local local) throws Exception {
        repository.delete(local);
    }
}
