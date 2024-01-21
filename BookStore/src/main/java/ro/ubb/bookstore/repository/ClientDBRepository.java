package ro.ubb.bookstore.repository;

import ro.ubb.bookstore.domain.Client;
import ro.ubb.bookstore.domain.validators.Validator;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDBRepository implements Repository<Long, Client> {

    private Validator<Client> validator;

    public ClientDBRepository(Validator<Client> validator) {
        this.validator = validator;
    }

    public Optional<Client> findOne(Long aLong) {
        Client client = new Client();
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
            String sqlString = "select * from clients where client_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Long id = resultSet.getLong("client_id");
                client.setId(id);

                String firstname = resultSet.getString("firstname");
                client.setFirstname(firstname);

                String lastname = resultSet.getString("lastname");
                client.setLastname(lastname);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(client);
    }

    @Override
    public Iterable<Client> findAll() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
            String sqlString = "select * from clients";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Client> clientsList = new ArrayList<>();

            while (resultSet.next()) {
                Client client = new Client();
                Long id = resultSet.getLong("client_id");
                client.setId(id);

                String firstname = resultSet.getString("firstname");
                client.setFirstname(firstname);

                String lastname = resultSet.getString("lastname");
                client.setLastname(lastname);

                clientsList.add(client);
            }
            return clientsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");

            String sqlString = "insert into clients( firstname, lastname) values (?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);

            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());

            preparedStatement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        String sqlUpdate = "delete from clients where client_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);

        ) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        String sqlUpdate = "update clients set firstname = ?, lastname = ? where client_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);

        ) {

            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setLong(3, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}


