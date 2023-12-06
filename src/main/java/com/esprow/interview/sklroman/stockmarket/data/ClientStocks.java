package com.esprow.interview.sklroman.stockmarket.data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class ClientStocks {

    @EmbeddedId
    private ClientStocksId id;
    private Long qty;

    public ClientStocks(ClientStocksId id, Long qty) {
        this.id = id;
        this.qty = qty;
    }

    public ClientStocks() {
    }

    public synchronized void increaseQty(Integer increment) {
        qty = qty + increment;
    }

    public synchronized void reduceQty(Integer decrement) {
        qty = qty - decrement;
    }

    public ClientStocksId getId() {
        return id;
    }

    public Long getQty() {
        return qty;
    }

    @Override
    public String toString() {
        return "ClientStocks{" +
                "id=" + id +
                ", qty=" + qty +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientStocks that = (ClientStocks) o;
        return Objects.equals(id, that.id) && Objects.equals(qty, that.qty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qty);
    }
}
