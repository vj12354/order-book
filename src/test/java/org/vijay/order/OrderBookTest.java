package org.vijay.order;

import org.junit.jupiter.api.BeforeEach;
import org.vijay.order.data.Order;

import static org.vijay.order.OrderBook.BID;

public abstract class OrderBookTest {
    protected static int ORDER_ID = 0;
    protected static final double PRICE = 10.0d;

    protected OrderBook orderBook;
    protected Order order;

    @BeforeEach
    public void init() {
        orderBook = new OrderBook();
        order = newOrder(BID, 10);
    }

    protected Order newOrder(char side, long size) {
        return newOrder(++ORDER_ID, PRICE, side, size);
    }

    protected Order newOrder(char side, double price) {
        return newOrder(++ORDER_ID, price, side, 1);
    }

    protected Order newOrder(long id, double price, char side, long size) {
        return new Order(id, price, side, size);
    }

}
