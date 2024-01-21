package ro.ubb.bookstore.domain;

import java.util.Optional;

public class SoldBooksDTO {
    public Optional<Book> book;
    public int quantity;

    public SoldBooksDTO() {
    }

    public SoldBooksDTO(Optional<Book> book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Optional<Book> getBook() {
        return book;
    }

    public void setBook(Optional<Book> book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return  book +
                ", quantity=" + quantity +
                '}' + "\n";
    }
}
