package ro.ubb.bookstore.service;

import ro.ubb.bookstore.domain.Book;
import ro.ubb.bookstore.domain.Client;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import ro.ubb.bookstore.repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, Client> clientRepository;

    public ClientService(Repository<Long, Client> clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void add(Client client) throws ValidatorException {
        clientRepository.save(client);
    }

    public void updateClient(Client client) {
        this.clientRepository.update(client);
    }

    /**
     * Gets a list of all Client objects.
     *
     * @return a list of all Client objects.
     */
    public Set<Client> getClients() {
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    public Optional<Client> getClient(Long id) {
        Optional<Client> client = this.clientRepository.findOne(id);
        return client;
    }

    /**
     * Delete a Client object.
     *
     * @param id of the Client object to delete.
     * @throws Exception if the id doesn't match any existent id.
     */
    public void deleteClient(long id) {
        this.clientRepository.delete(id);
    }

//    public Set<Client> searchClient(String text) {
//        text = text.toLowerCase();
//        Set<Client> clients = this.getClients();
//        Set<Client> searched = new HashSet<>();
//        for (Client c : clients) {
//            String id = String.valueOf(c.getId());
//            String firstname = c.getFirstname().toLowerCase();
//            String lastname = c.getLastname().toLowerCase();
//            if (id.contains(text) || firstname.contains(text) || lastname.contains(text)) {
//                searched.add(c);
//            }
//        }
//        return searched;
//    }

    public Set<Client> searchClient(String s) {
        Set<Client> clientSet = this.getClients();
        return clientSet.stream().filter(b -> b.getFirstname().toLowerCase().contains(s))
                                .filter(b -> b.getLastname().toLowerCase().contains(s))
                                .collect(Collectors.toSet());
    }
}
