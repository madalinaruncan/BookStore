package ro.ubb.bookstore.service;

import ro.ubb.bookstore.domain.Book;
import ro.ubb.bookstore.domain.validators.ValidatorException;
import ro.ubb.bookstore.repository.Repository;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService {
    private Repository<Long, Book> bookRepository;

    public BookService(Repository<Long, Book> bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) throws ValidatorException {
        bookRepository.save(book);
    }


    public void updateBook(Book book) {
        this.bookRepository.update(book);
    }

    public Optional<Book> getBook(Long id) {
        Optional<Book> book = this.bookRepository.findOne(id);
        return book;
    }

    /**
     * Gets a list of all Book objects.
     *
     * @return a list of all Book objects.
     */
    public Set<Book> getBooks() {
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    /**
     * Delete a Book object.
     *
     * @param id of the Book object to delete.
     * @throws Exception if the id doesn't match any existent id.
     */
    public void deleteBook(long id) throws ValidatorException {
        this.bookRepository.delete(id);
    }

    public Set<Book> searchBook(String text) {
        text = text.toLowerCase();
        Set<Book> books = this.getBooks();
        Set<Book> searched = new HashSet<>();
        for (Book b : books) {
            String id = String.valueOf(b.getId());
            String title = b.getTitle().toLowerCase();
            String author_firstname = b.getAuthor_firstname().toLowerCase();
            String author_lastname = b.getAuthor_lastname().toLowerCase();
            String released_year = String.valueOf(b.getRelease_year());
            String price = String.valueOf(b.getPrice());
            if (id.contains(text) || title.contains(text) || author_firstname.contains(text) || author_lastname.contains(text) || released_year.contains(text) || price.contains(text)) {
                searched.add(b);
            }
        }
        return searched;
    }

    public List<Book> groupBooksByYear(int year) {
        Set<Book> bookSet = this.getBooks();
        List<Book> bookList = bookSet.stream().toList();

        bookList.stream().map(b -> b.getRelease_year() > year).collect(Collectors.toSet());



        List<Book> newerBooks = bookList.stream()
                .filter(book -> book.getRelease_year() > year)
                .collect(Collectors.toList());

        List<Book> sameYearBooks = bookList.stream()
                .filter(book -> book.getRelease_year() == year)
                .collect(Collectors.toList());

        List<Book> olderBooks = bookList.stream()
                .filter(book -> book.getRelease_year() < year)
                .collect(Collectors.toList());

        List<Book> result = new ArrayList<>();
        result.addAll(newerBooks);
        result.addAll(sameYearBooks);
        result.addAll(olderBooks);
        return bookList.stream().collect(Collectors.partitioningBy(book -> book.getRelease_year() > year))
                .values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    public List<Book> groupBooksByYearAlphabetical(int year){
        Set<Book> bookSet = this.getBooks();
        List<Book> bookList = bookSet.stream().toList();

        List<Book> olderBooks = bookList.stream()
                .filter(book -> book.getRelease_year() < year).sorted((b1,b2) -> b1.getTitle().compareTo(b2.getTitle()))
                .collect(Collectors.toList());

        List<Book> newerBooks = bookList.stream()
                .filter(book -> book.getRelease_year() >= year).sorted((Comparator.comparingInt(Book::getRelease_year)))
                .collect(Collectors.toList());

        List<Book> result = new ArrayList<>();
        result.addAll(olderBooks);
        result.addAll(newerBooks);


        return result;
    }
}
