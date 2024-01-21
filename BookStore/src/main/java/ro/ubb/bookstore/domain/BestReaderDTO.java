package ro.ubb.bookstore.domain;

import java.util.Optional;

public class BestReaderDTO {
    public Optional<Client> client;
    public int bookNumber;

    public BestReaderDTO() {
    }

    public BestReaderDTO(Optional<Client> client, int bookNumber) {
        this.client = client;
        this.bookNumber = bookNumber;
    }

    public Optional<Client> getClient() {
        return client;
    }

    public void setClient(Optional<Client> client) {
        this.client = client;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    @Override
    public String toString() {
        return "BestReaderDTO{" +
                "client=" + client +
                ", bookNumber=" + bookNumber +
                '}';
    }
}
