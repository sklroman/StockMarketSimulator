package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.enums.OrderStatus;
import com.esprow.interview.sklroman.stockmarket.enums.OrderType;
import com.esprow.interview.sklroman.stockmarket.enums.TradeType;
import com.esprow.interview.sklroman.stockmarket.error.OrderCrossingException;
import com.esprow.interview.sklroman.stockmarket.tool.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

public class Order {

    private final Logger logger = LogManager.getLogger();

    private final String id;
    private final Long created;
    private String clientId;
    private OrderStatus status;
    @NotNull
    private String symbol;
    @NotNull
    private TradeType tradeType;
    @NotNull
    private OrderType orderType;
    @NotNull
    private Integer cost;   //price in cents
    @NotNull
    private Integer qty;    //ordered quantity in lots
    private Integer stock;  //current stock of lots

    public void reduceStock(Integer diff) {
        if (diff > stock) {
            logger.debug(" Diff: " + diff + ", stock: " + stock);
            throw new OrderCrossingException("Diff cant be more than stock");
        }
        stock = stock - diff;
    }

    public Order() {
        id = Tools.generateOrderId();
        status = OrderStatus.NEW;
        created = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        logger.info("Client ID: {}, Order ID: {}, status {} setted ", getClientId(), this.getId(), status.toString());
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    @Profile("dev")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        stock = qty;
        this.qty = qty;
    }

    public Integer getStock() {
        return stock;
    }

    public Long getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id.substring(0, 3) + '\'' +
                ", created=" + created.toString().substring(7, 12) +
                ", clientId='" + clientId + '\'' +
                ", status=" + status +
//                ", symbol='" + symbol + '\'' +
                ", tradeType=" + tradeType +
//                ", orderType=" + orderType +
                ", cost=" + cost +
                ", qty=" + qty +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
