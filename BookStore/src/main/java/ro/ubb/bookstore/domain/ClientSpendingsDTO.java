package ro.ubb.bookstore.domain;

import java.util.Optional;

public class ClientSpendingsDTO {
    public Optional<Client> client;
    public Double spendings;

    public ClientSpendingsDTO() {
    }

    public ClientSpendingsDTO(Optional<Client> client, Double spendings) {
        this.client = client;
        this.spendings = spendings;
    }

    public Optional<Client> getClient() {
        return client;
    }

    public void setClient(Optional<Client> client) {
        this.client = client;
    }

    public Double getSpendings() {
        return spendings;
    }

    public void setSpendings(Double spendings) {
        this.spendings = spendings;
    }

    @Override
    public String toString() {
        return "ClientSpendingsDTO{" +
                "client=" + client +
                ", spendings=" + spendings +
                '}' + "\n";
    }
}
