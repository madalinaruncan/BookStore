package ro.ubb.bookstore.ui;

import ro.ubb.bookstore.domain.*;
import ro.ubb.bookstore.service.BookService;
import ro.ubb.bookstore.service.ClientService;
import ro.ubb.bookstore.service.TransactionService;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Console {
    private BookService bookService;
    private ClientService clientService;
    private TransactionService transactionService;
    private Scanner scanner;

    public Console(BookService bookService, ClientService clientService, TransactionService transactionService) {
        this.bookService = bookService;
        this.clientService = clientService;
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }


    private void showMenu() {
        System.out.println("1. Book operations");
        System.out.println("2. Client operations");
        System.out.println("3. Transaction operations");
        System.out.println("4. Show sale reports");
        System.out.println("5. Show clients ordered by spending");
        System.out.println("6. Show best reader");
        System.out.println("0. Leave program");
        System.out.println("Enter your option: ");
    }

    public void runConsole() {
        while (true) {
            this.showMenu();
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.runSubmenuCRUDBook();
                    break;
                case 2:
                    this.runSubmenuCRUDClient();
                    break;
                case 3:
                    this.runSubmenuTransactions();
                    break;
                case 4:
                    this.handleShowSaleReport();
                    break;
                case 5:
                    this.handleShowSpendingReport();
                    break;
                case 6:
                    this.handleShowBestReader();
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void handleShowBestReader() {
        System.out.println(transactionService.bestReader());
    }

    private void handleShowSpendingReport() {
        List<ClientSpendingsDTO> spendings = transactionService.getClientSpendingsReport();
        System.out.println(spendings);
    }

    private void handleShowSaleReport() {
        List<SoldBooksDTO> soldBooks = transactionService.getBooksSoldQuantity();
        System.out.println(soldBooks);
    }

    private void runSubmenuTransactions() {
        while (true) {
            System.out.println("1. Add a new transaction");
            System.out.println("2. Update a transaction");
            System.out.println("3. Delete a transaction");
            System.out.println("4. Show transactions");
            System.out.println("5. Find one transaction");
            System.out.println("0. Back");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.handleAddTransaction();
                    break;
                case 2:
                    this.handleUpdateTransaction();
                    break;
                case 3:
                    this.handleDeleteTransaction();
                    break;
                case 4:
                    this.showAll(this.transactionService.getTransactions());
                    break;
                case 5:
                    this.handleFindTransactionById();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void handleDeleteTransaction() {
        try {
            System.out.print("Enter the id of the transaction you want to delete: ");
            Long id = scanner.nextLong();
            this.transactionService.deleteTransaction(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleUpdateTransaction() {
        System.out.println("Enter the id of the transaction you want to update: ");
        Long id = scanner.nextLong();

        System.out.print("Enter client's id: ");
        Long client_id = scanner.nextLong();

        System.out.print("Enter book's id: ");
        Long book_id = scanner.nextLong();

        System.out.print("Enter the quantity: ");
        int quantity = scanner.nextInt();

        Transaction transactionToUpdate = new Transaction(client_id, book_id, quantity);
        transactionToUpdate.setId(id);
        this.transactionService.updateTransaction(transactionToUpdate);
    }

    private void handleFindTransactionById() {
        System.out.println("Enter transaction's id: ");
        Long id = scanner.nextLong();
        System.out.println(this.transactionService.getTransaction(id));
    }

    private void handleAddTransaction() {
        System.out.print("Enter client's id: ");
        Long client_id = scanner.nextLong();

        System.out.print("Enter book's id: ");
        Long book_id = scanner.nextLong();

        System.out.print("Enter the quantity: ");
        int quantity = scanner.nextInt();

        Transaction transactionToAdd = new Transaction(client_id, book_id, quantity);

        this.transactionService.addTransaction(transactionToAdd);
    }


    private void runSubmenuCRUDBook() {
        while (true) {
            System.out.println("1. Add a book");
            System.out.println("2. Update a book");
            System.out.println("3. Delete a book");
            System.out.println("4. Show books");
            System.out.println("5. Find book by id");
            System.out.println("6. Find book");
            System.out.println("7. Arrange books by year");
            System.out.println("8. Arrange older books alphabetical and newer books by year");
            System.out.println("0. Back");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.handleAddBook();
                    break;
                case 2:
                    this.handleUpdateBook();
                    break;
                case 3:
                    this.handleDeleteBook();
                    break;
                case 4:
                    this.showAll(this.bookService.getBooks());
                    break;
                case 5:
                    this.findBookById();
                    break;
                case 6:
                    this.findBook();
                    break;
                case 7:
                    this.arrangeBooks();
                    break;
                case 8:
                    this.arrangeBooksAlphabetical();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void arrangeBooksAlphabetical() {
        System.out.println("Enter a year: ");
        int year = scanner.nextInt();
        List<Book> books = this.bookService.groupBooksByYearAlphabetical(year);
        this.showAll(books);
    }

    private void arrangeBooks() {
        System.out.println("Enter a year: ");
        int year = scanner.nextInt();
        List<Book> books = this.bookService.groupBooksByYear(year);
        this.showAll(books);
    }

    private void findBook() {
        System.out.println("Enter one or many key words:");
        scanner.nextLine();
        String searchedText = scanner.nextLine();
        Set<Book> books = this.bookService.searchBook(searchedText);
        if (!books.isEmpty()) {
            for (Book b : books) {
                System.out.println(b);
            }
        }
    }

    private void showAll(Iterable objects) {
        for (Object obj : objects) {
            System.out.println(obj);
        }
    }

    private void findBookById() {
        System.out.println("Enter book's id: ");
        Long id = scanner.nextLong();
        System.out.println(this.bookService.getBook(id));
    }

    private void handleAddBook() {
        try {
            scanner.nextLine();
            System.out.print("Enter the title: ");
            String title = scanner.nextLine();

            System.out.print("Enter the author's firstname: ");
            String author_firstname = scanner.nextLine();

            System.out.print("Enter the author's lastname: ");
            String author_lastname = scanner.nextLine();

            System.out.print("Enter the release year: ");
            int release_year = scanner.nextInt();

            System.out.print("Enter the price: ");
            double price = scanner.nextDouble();

            Book bookToAdd = new Book(title, author_firstname, author_lastname, release_year, price);

            this.bookService.addBook(bookToAdd);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleUpdateBook() {
        try {
            System.out.println("Enter the id of the book you want to update:");
            Long id = scanner.nextLong();
            scanner.nextLine();
            System.out.print("Enter the updated title: ");
            String title = scanner.nextLine();

            System.out.print("Enter updated author firstname: ");
            String author_firstname = scanner.nextLine();

            System.out.print("Enter updated author lastname: ");
            String author_lastname = scanner.nextLine();

            System.out.print("Enter updated release year: ");
            int release_year = scanner.nextInt();

            System.out.print("Enter updated price: ");
            double price = scanner.nextDouble();

            Book bookToUpdate = new Book(title, author_firstname, author_lastname, release_year, price);
            bookToUpdate.setId(id);
            this.bookService.updateBook(bookToUpdate);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleDeleteBook() {
        try {
            System.out.print("Enter the id: ");
            int id = scanner.nextInt();
            this.bookService.deleteBook(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void runSubmenuCRUDClient() {
        while (true) {
            System.out.println("1. Add client");
            System.out.println("2. Update client");
            System.out.println("3. Delete client");
            System.out.println("4. Show clients");
            System.out.println("5. Find client by id");
            System.out.println("6. Find client");
            System.out.println("0. Back");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    this.handleAddClient();
                    break;
                case 2:
                    this.handleUpdateClient();
                    break;
                case 3:
                    this.handleDeleteClient();
                    break;
                case 4:
                    this.showAll(this.clientService.getClients());
                    break;
                case 5:
                    this.findClientById();
                    break;
                case 6:
                    this.findClient();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void findClient() {
        System.out.println("Enter one or many key words:");
        scanner.nextLine();
        String searchedText = scanner.nextLine();
        Set<Client> clients = this.clientService.searchClient(searchedText);
        if (!clients.isEmpty()) {
            for (Client c : clients) {
                System.out.println(c);
            }
        }

    }

    private void findClientById() {
        System.out.println("Enter client's id: ");
        Long id = scanner.nextLong();
        System.out.println(this.clientService.getClient(id));
    }

    private void handleAddClient() {
        try {
            System.out.print("Enter the first name: ");
            String firstname = scanner.next();

            System.out.print("Enter the last name: ");
            String lastname = scanner.next();

            Client clientToAdd = new Client(firstname, lastname);
            this.clientService.add(clientToAdd);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleUpdateClient() {
        try {
            System.out.println("Enter the id of the client you want to update:");
            Long id = scanner.nextLong();

            System.out.print("Enter the first name: ");
            String firstname = scanner.next();

            System.out.print("Enter the last name: ");
            String lastname = scanner.next();
            Client clientToUpdate = new Client(firstname, lastname);
            clientToUpdate.setId(id);
            this.clientService.updateClient(clientToUpdate);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void handleDeleteClient() {
        try {
            System.out.print("Enter the id: ");
            int id = scanner.nextInt();
            this.clientService.deleteClient(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
