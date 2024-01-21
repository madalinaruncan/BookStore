package ro.ubb.bookstore.repository;

import ro.ubb.bookstore.domain.Book;
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

public class BookFileRepository extends InMemoryRepository<Long, Book> {
    private String bookFile;

    public BookFileRepository(Validator<Book> bookValidator, String bookFile){
        super(bookValidator);
        this.bookFile = bookFile;
        loadData();
    }

    private void loadData() {
        Path path = Paths.get(bookFile);

        try {
            Files.lines(path).forEach(line -> {
                List<String> books = Arrays.asList(line.split(","));

                Long id = Long.valueOf(books.get(0));
                String title = books.get(1);
                String author_firstname = books.get(2);
                String author_lastname = books.get(3);
                int release_year = Integer.parseInt(books.get(4));
                double price = Double.parseDouble(books.get(5));

                Book book = new Book(title, author_firstname, author_lastname, release_year, price);
                book.setId(id);
                try {
                    super.save(book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(Book entity) {
        Path path = Paths.get(bookFile);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(entity.getId() + "," + entity.getTitle() + "," + entity.getAuthor_firstname() + "," + entity.getAuthor_lastname() + "," + entity.getRelease_year());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

