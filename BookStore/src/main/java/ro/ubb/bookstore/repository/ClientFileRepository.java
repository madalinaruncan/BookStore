package ro.ubb.bookstore.repository;

import ro.ubb.bookstore.domain.Client;
import ro.ubb.bookstore.domain.validators.Validator;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClientFileRepository extends InMemoryRepository<Long, Client> {
    private String clientFile;

    public ClientFileRepository(Validator<Client> clientValidator, String clientFile){
        super(clientValidator);
        this.clientFile = clientFile;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(clientFile);

        try {
            Files.lines(path).forEach(line -> {
                List<String> clients = Arrays.asList(line.split(","));

                Long id = Long.valueOf(clients.get(0));
                String firstname = clients.get(1);
                String lastname = clients.get(2);


                Client client = new Client(firstname, lastname);
                client.setId(id);
                try {
                    super.save(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Client entity) {
        Path path = Paths.get(clientFile);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(entity.getId() + "," + entity.getFirstname() + "," + entity.getLastname() );
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
