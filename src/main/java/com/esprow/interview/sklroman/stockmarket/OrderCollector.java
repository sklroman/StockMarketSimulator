package com.esprow.interview.sklroman.stockmarket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderCollector {

    private Map<Integer, ConcurrentLinkedQueue<Order>> collector = new ConcurrentHashMap<>();

    public void place(Order order) {
        if (collector.containsKey(order.getCost())) {
            collector.get(order.getCost()).add(order);
        } else {
            ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<>();
            queue.add(order);
            collector.put(order.getCost(), queue);
        }
    }

    public Map<Integer, ConcurrentLinkedQueue<Order>> get() {
        return collector;
    }

    public void reset() {
        collector = new ConcurrentHashMap<>();
    }
}
