package ro.ubb.bookstore.repository;

import ro.ubb.bookstore.domain.Book;
import ro.ubb.bookstore.domain.validators.Validator;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDBRepository implements Repository<Long, Book> {
    private Validator<Book> validator;

    public BookDBRepository(Validator<Book> validator) {
        this.validator = validator;
    }

    @Override
    public Optional<Book> findOne(Long aLong) {
        Book book = new Book();
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
            String sqlString = "select * from books where book_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("book_id");
                book.setId(id);

                String title = resultSet.getString("title");
                book.setTitle(title);

                String author_firstname = resultSet.getString("author_firstname");
                book.setAuthor_firstname(author_firstname);

                String author_lastname = resultSet.getString("author_lastname");
                book.setAuthor_lastname(author_lastname);

                Integer release_year = resultSet.getInt("release_year");
                book.setRelease_year(release_year);

                Double price = resultSet.getDouble("price");
                book.setPrice(price);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(book);
    }

    @Override
    public Iterable<Book> findAll() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
            String sqlString = "select * from books";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> booksList = new ArrayList<>();

            while (resultSet.next()) {
                Book book = new Book();
                Long id = resultSet.getLong("book_id");
                book.setId(id);

                String title = resultSet.getString("title");
                book.setTitle(title);

                String author_firstname = resultSet.getString("author_firstname");
                book.setAuthor_firstname(author_firstname);

                String author_lastname = resultSet.getString("author_lastname");
                book.setAuthor_lastname(author_lastname);

                Integer release_year = resultSet.getInt("release_year");
                book.setRelease_year(release_year);

                Double price = resultSet.getDouble("price");
                book.setPrice(price);

                booksList.add(book);
            }
            return booksList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");

            String sqlString = "insert into books(title, author_firstname, author_lastname, release_year, price) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);

            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getAuthor_firstname());
            preparedStatement.setString(3, entity.getAuthor_lastname());
            preparedStatement.setInt(4, entity.getRelease_year());
            preparedStatement.setDouble(5, entity.getPrice());

            preparedStatement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Book> delete(Long aLong) {
        String sqlUpdate = "delete from books where book_id = ?";
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
    public Optional<Book> update(Book entity) throws ValidatorException {
        String sqlUpdate = "update books set title = ?, author_firstname = ?, author_lastname = ?, release_year = ?, price = ? where book_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlUpdate);

        ) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setString(2, entity.getAuthor_firstname());
            preparedStatement.setString(3, entity.getAuthor_lastname());
            preparedStatement.setInt(4, entity.getRelease_year());
            preparedStatement.setDouble(5, entity.getPrice());
            preparedStatement.setLong(6, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }
}
