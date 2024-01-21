package ro.ubb.bookstore;

import ro.ubb.bookstore.domain.Book;
import ro.ubb.bookstore.domain.Client;
import ro.ubb.bookstore.domain.Transaction;
import ro.ubb.bookstore.domain.validators.BookValidator;
import ro.ubb.bookstore.domain.validators.ClientValidator;
import ro.ubb.bookstore.domain.validators.Validator;
import ro.ubb.bookstore.repository.*;
import ro.ubb.bookstore.service.BookService;
import ro.ubb.bookstore.service.ClientService;
import ro.ubb.bookstore.service.TransactionService;
import ro.ubb.bookstore.ui.Console;

public class Main {
    public static void main(String[] args) {

        Validator<Book> bookValidator = new BookValidator();
        Repository<Long, Book> bookRepository = new BookDBRepository(bookValidator);
        //Repository<Long, Book> bookRepository = new BookXMLRepository(bookValidator, "./data/books.xml");
        //Repository<Long, Book> bookRepository = new BookFileRepository(bookValidator, "./data/books");
        BookService bookService = new BookService(bookRepository);
        Validator<Client> clientValidator = new ClientValidator();
        //Repository<Long, Client> clientRepository = new ClientFileRepository(clientValidator,"./data/clients");
        //Repository<Long, Client> clientRepository = new ClientXMLRepository(clientValidator, "./data/clients.xml");
        Repository<Long, Client> clientRepository = new ClientDBRepository(clientValidator);
        ClientService clientService = new ClientService(clientRepository);
        Repository<Long, Transaction> transactionRepository = new TransactionDBRepository();
        TransactionService transactionService = new TransactionService(transactionRepository, bookRepository, clientRepository);

        Console console = new Console(bookService, clientService, transactionService);

    console.runConsole();
    }
}