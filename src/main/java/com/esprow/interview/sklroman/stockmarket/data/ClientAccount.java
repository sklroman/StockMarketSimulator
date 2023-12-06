package com.esprow.interview.sklroman.stockmarket.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ClientAccount {

    @Id
    private String clientId;
    private Long amount;

    public ClientAccount(String clientId, Long amount) {
        this.clientId = clientId;
        this.amount = amount;
    }

    public ClientAccount() {
    }

    public synchronized void increaseAmount(Integer increment) {
        amount = amount + increment;
    }

    public synchronized void reduceAmount(Integer decrement) {
        amount = amount - decrement;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "ClientAccount{" +
                "clientId='" + clientId + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAccount that = (ClientAccount) o;
        return Objects.equals(clientId, that.clientId) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, amount);
    }
}
