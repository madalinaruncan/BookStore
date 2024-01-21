package ro.ubb.bookstore.domain;


public class Transaction extends BaseEntity<Long>{
    private Long client_id;
    private Long book_id;
    private int quantity;

    public Transaction(){}

    public Transaction(Long client_id, Long book_id, int quantity) {
        this.client_id = client_id;
        this.book_id = book_id;
        this.quantity = quantity;
    }

    public Long getClient_id() {
        return client_id;
    }

    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "client_id=" + client_id +
                ", book_id=" + book_id +
                ", quantity=" + quantity +
                '}';
    }
}
