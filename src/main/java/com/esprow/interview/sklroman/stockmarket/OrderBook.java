package com.esprow.interview.sklroman.stockmarket;

import com.esprow.interview.sklroman.stockmarket.enums.OrderStatus;
import com.esprow.interview.sklroman.stockmarket.tool.Flags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderBook {

    private AtomicBoolean isExecutingNow = new AtomicBoolean(false);
    private final String symbol;
    private final OrderCollector sellTradeOrderCollector = new OrderCollector();
    private final OrderCollector buyTradeOrderCollector = new OrderCollector();
    private final TreeMap<Integer, LinkedList<Order>> forSell = new TreeMap<>();
    private final TreeMap<Integer, LinkedList<Order>> forBuy = new TreeMap<>();
    private final Map<String, Order> ordersForUpdate = new ConcurrentHashMap<>();

    private final Logger logger = LogManager.getLogger();

    private Integer last = 0; //last trade price

    public OrderBook(String symbol) {
        this.symbol = symbol;
    }

    public boolean isExecutingNow() {
        return isExecutingNow.get();
    }

    public void setExecutingNow(boolean isExecutingNow) {
        this.isExecutingNow.set(isExecutingNow);
    }

    public String getSymbol() {
        return symbol;
    }

    public void placeBuyTradeOrder(Order order) {
        buyTradeOrderCollector.place(order);
        ordersForUpdate.put(order.getId(), order);
        logger.info("Placed BUY order: " + order);
    }

    public void placeSellTradeOrder(Order order) {
        sellTradeOrderCollector.place(order);
        ordersForUpdate.put(order.getId(), order);
        logger.info("Placed SELL order: " + order);
    }

    public void cancelOrder(String orderId) {
        if (ordersForUpdate.containsKey(orderId)) {
            ordersForUpdate.get(orderId).setStatus(OrderStatus.CANCELLED_BY_CLIENT);
        }
    }

    public void preparedForExecution() {
        moveNewOrders(buyTradeOrderCollector, forBuy);
        moveNewOrders(sellTradeOrderCollector, forSell);
    }

    public void cleaning() {
        clean(forBuy);
        clean(forSell);
    }

    public TreeMap<Integer, LinkedList<Order>> getForBuy() {
        return forBuy;
    }

    public TreeMap<Integer, LinkedList<Order>> getForSell() {
        return forSell;
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public void log() {
        debug();
        devPrint();
    }

    private void clean(TreeMap<Integer, LinkedList<Order>> map) {
        for (LinkedList<Order> orders : map.values()) {
            orders.removeIf(order -> !OrderStatus.NEW.equals(order.getStatus())
                    && !OrderStatus.PARTIALLY_COMPLETED.equals(order.getStatus()));
        }
    }

    private void moveNewOrders(OrderCollector collector, TreeMap<Integer, LinkedList<Order>> orders) {
        Map<Integer, ConcurrentLinkedQueue<Order>> newOrders = collector.get();
        collector.reset();
        for (Integer key : newOrders.keySet()) {
            if (orders.containsKey(key)) {
                orders.get(key).addAll(newOrders.get(key));
            } else {
                LinkedList<Order> queue = new LinkedList<>(newOrders.get(key));
                orders.put(key, queue);
            }
        }
    }

    private void debug() {
        if (logger.isDebugEnabled()) {
            logger.debug(" >> ORDER BOOK: ");
            int totalSellCounter = 0;
            for (Integer key : forSell.descendingKeySet()) {
                LinkedList<Order> orders = forSell.get(key);
                int stockCounter = 0;
                for (Order order : orders) {
                    stockCounter += order.getStock();
                    totalSellCounter += order.getStock();
                }
                logger.debug(" SELL: " + key + " | " + forSell.get(key).size() + " | " + stockCounter + " | " + forSell.get(key));
            }
            logger.debug(" total stocks for sell: " + totalSellCounter);
            logger.debug("--------------------------------------");
            int totalBuyCounter = 0;
            for (Integer key : forBuy.descendingKeySet()) {
                LinkedList<Order> orders = forBuy.get(key);
                int stockCounter = 0;
                for (Order order : orders) {
                    stockCounter += order.getStock();
                    totalBuyCounter += order.getStock();
                }
                logger.debug(" BUY: " + key + " | " + forBuy.get(key).size() + " | " + stockCounter + " | " + forBuy.get(key));
            }
            logger.debug(" total stocks for buy: " + totalBuyCounter);
            logger.debug(" LAST: " + getLast());
            logger.debug("++++++++++++++++++END++++++++++++++++++");
        }
    }

    private void devPrint() {
        if (Flags.isDev()) {
            System.out.println(" >> ORDER BOOK: ");
            int totalSellCounter = 0;
            for (Integer key : forSell.descendingKeySet()) {
                LinkedList<Order> orders = forSell.get(key);
                int stockCounter = 0;
                for (Order order : orders) {
                    stockCounter += order.getStock();
                    totalSellCounter += order.getStock();
                }
                System.out.println(" SELL: " + key + " | " + forSell.get(key).size() + " | " + stockCounter + " | " + forSell.get(key));
            }
            System.out.println(" total stocks for sell: " + totalSellCounter);
            System.out.println("--------------------------------------");
            int totalBuyCounter = 0;
            for (Integer key : forBuy.descendingKeySet()) {
                LinkedList<Order> orders = forBuy.get(key);
                int stockCounter = 0;
                for (Order order : orders) {
                    stockCounter += order.getStock();
                    totalBuyCounter += order.getStock();
                }
                System.out.println(" BUY: " + key + " | " + forBuy.get(key).size() + " | " + stockCounter + " | " + forBuy.get(key));
            }
            System.out.println(" total stocks for buy: " + totalBuyCounter);
            System.out.println(" LAST: " + getLast());
            System.out.println("++++++++++++++++++END++++++++++++++++++");
        }
    }
}
