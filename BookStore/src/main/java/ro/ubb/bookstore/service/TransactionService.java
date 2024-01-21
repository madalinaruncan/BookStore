package ro.ubb.bookstore.service;

import ro.ubb.bookstore.domain.*;
import ro.ubb.bookstore.repository.Repository;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TransactionService {

    private Repository<Long, Transaction> transactionRepository;
    private Repository<Long, Book> bookRepository;
    private Repository<Long, Client> clientRepository;


    public TransactionService(Repository<Long, Transaction> transactionRepository, Repository<Long, Book> bookRepository, Repository<Long, Client> clientRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        this.transactionRepository.update(transaction);
    }

    public void deleteTransaction(Long transaction_id) {
        this.transactionRepository.delete(transaction_id);
    }

    public Optional<Transaction> getTransaction(Long transaction_id) {
        Optional<Transaction> transaction = this.transactionRepository.findOne(transaction_id);
        return transaction;
    }

    public Set<Transaction> getTransactions() {
        Iterable<Transaction> transactions = transactionRepository.findAll();
        return StreamSupport.stream(transactions.spliterator(), false).collect(Collectors.toSet());
    }

    public Optional<BestReaderDTO> bestReader() {
        String sqlString = "SELECT client_id, SUM(quantity) AS total_books_bought FROM transactions GROUP BY client_id ORDER BY total_books_bought DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bookstore", "postgres", "admin");
             PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<BestReaderDTO> bestReader = Optional.empty();

            while (resultSet.next()) {
                Long id = resultSet.getLong("client_id");
                int quantity = resultSet.getInt("total_books_bought");

                BestReaderDTO existingBestReader = new BestReaderDTO(clientRepository.findOne(id), quantity);
                bestReader = Optional.of(existingBestReader);
            }
            return bestReader;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientSpendingsDTO> getClientSpendingsReport() {
        Set<Transaction> transactions = this.getTransactions();
        Map<Long, Double> spendings = new HashMap<>();
        List<ClientSpendingsDTO> clientSpendings = new ArrayList<>();

        for(Transaction transaction : transactions){
            if(!spendings.containsKey(transaction.getClient_id())){
                Optional<Book> book = bookRepository.findOne(transaction.getBook_id());
                Double money = book.get().getPrice() * transaction.getQuantity();
               spendings.put(transaction.getClient_id(), money);
            } else {
                Optional<Book> book = bookRepository.findOne(transaction.getBook_id());
                Double money = book.get().getPrice() * transaction.getQuantity();
                Double spend = spendings.get(transaction.getClient_id()) + money;
                spendings.put(transaction.getClient_id(), spend);
            }
        }

        for (Long key : spendings.keySet()) {
            Optional<Client> client = clientRepository.findOne(key);
            ClientSpendingsDTO clientSpendingsDTO = new ClientSpendingsDTO(client, spendings.get(key));
            clientSpendings.add(clientSpendingsDTO);
        }

        Collections.sort(clientSpendings, Comparator.comparingDouble(ClientSpendingsDTO::getSpendings).reversed());

        return clientSpendings;
    }

    public List<SoldBooksDTO> getBooksSoldQuantity() {
        Set<Transaction> transactions = this.getTransactions();
        Map<Long, Integer> soldBooksMap = new HashMap<>();
        List<SoldBooksDTO> soldBooksList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (!soldBooksMap.containsKey(transaction.getBook_id())) {
                soldBooksMap.put(transaction.getBook_id(), transaction.getQuantity());
            } else {
                int quantity = soldBooksMap.get(transaction.getBook_id());
                quantity = quantity + transaction.getQuantity();
                soldBooksMap.put(transaction.getBook_id(), quantity);
            }
        }
        for (Long key : soldBooksMap.keySet()) {
            Optional<Book> book = bookRepository.findOne(key);
            SoldBooksDTO soldBooksDTO = new SoldBooksDTO(book, soldBooksMap.get(key));
            soldBooksList.add(soldBooksDTO);
        }

        Collections.sort(soldBooksList, Comparator.comparingInt(SoldBooksDTO::getQuantity).reversed());
        return soldBooksList;

    }
}
