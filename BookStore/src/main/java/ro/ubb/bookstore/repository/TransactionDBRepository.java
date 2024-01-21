package ro.ubb.bookstore.repository;

import ro.ubb.bookstore.domain.Transaction;
import ro.ubb.bookstore.domain.validators.ValidatorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDBRepository implements Repository<Long, Transaction> {

    @Override
    public Optional<Transaction> findOne(Long aLong) {
        String sqlString = "select * from transactions where transaction_id = ?";
        Transaction transaction = new Transaction();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
        ) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("transaction_id");
                transaction.setId(id);

                Long client_id = resultSet.getLong("client_id");
                transaction.setClient_id(client_id);

                Long book_id = resultSet.getLong("book_id");
                transaction.setBook_id(book_id);

                int quantity = resultSet.getInt("quantity");
                transaction.setQuantity(quantity);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(transaction);
    }

    @Override
    public Iterable<Transaction> findAll() {
        String sqlString = "select * from transactions";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactionList = new ArrayList<>();

            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                Long id = resultSet.getLong("transaction_id");
                transaction.setId(id);

                Long client_id = resultSet.getLong("client_id");
                transaction.setClient_id(client_id);

                Long book_id = resultSet.getLong("book_id");
                transaction.setBook_id(book_id);

                int quantity = resultSet.getInt("quantity");
                transaction.setQuantity(quantity);

                transactionList.add(transaction);
            }
            return transactionList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> save(Transaction entity) throws ValidatorException {

        String sqlString = "insert into transactions(client_id, book_id, quantity) values (?,?,?)";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
        ) {
            preparedStatement.setLong(1, entity.getClient_id());
            preparedStatement.setLong(2, entity.getBook_id());
            preparedStatement.setInt(3, entity.getQuantity());

            preparedStatement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> delete(Long aLong) {
        String sqlUpdate = "delete from books where transaction_id = ?";
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
    public Optional<Transaction> update(Transaction entity) throws ValidatorException {
        String sqlUpdate = "update transactions set transaction_id = ?, client_id = ?, book_id = ?, quantity = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);

        ) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getClient_id());
            preparedStatement.setLong(3, entity.getBook_id());
            preparedStatement.setInt(4, entity.getQuantity());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
