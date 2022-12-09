package org.vijay.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vijay.order.data.Order;
import org.vijay.order.exception.InvalidOrderException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.vijay.order.OrderBook.BID;
import static org.vijay.order.OrderBook.OFFER;

public class OrderBookAddTest extends OrderBookTest{

    @Test
    public void addOrder_fails_whenSize_is_empty() {
        order = newOrder(BID, 0);
        InvalidOrderException exception = Assertions.assertThrows(
                InvalidOrderException.class, () -> orderBook.addOrder(order));
        assertThat(exception.getMessage()).isEqualTo("Zero size");
    }

    @Test
    public void addOrder_fails_whenPrice_is_negative() {
        order = newOrder(1, -10, BID, 1);
        InvalidOrderException exception = Assertions.assertThrows(
                InvalidOrderException.class, () -> orderBook.addOrder(order));
        assertThat(exception.getMessage()).isEqualTo("Negative price not accepted yet.");
    }

    @Test
    public void addOrder_fails_whenSide_is_invalid() {
        order = newOrder('X', 10);
        InvalidOrderException exception = Assertions.assertThrows(
                InvalidOrderException.class, () -> orderBook.addOrder(order));
        assertThat(exception.getMessage()).isEqualTo("Incorrect Side");
    }

    @Test
    public void add_valid_order_successful() {
        orderBook.addOrder(order);

        sleep(2000);
        List<Order> orders = orderBook.getOrders(order.getSide());

        assertThat(orders.stream().filter(it -> it.getId() == order.getId())
                .count()).isEqualTo(1);

        Optional<Order> added = orders.stream()
                .filter(it -> it.getId() == order.getId()).findAny();
        assertThat(added.isPresent()).isTrue();
        assertThat(added.get().getPrice()).isEqualTo(order.getPrice());
    }

    @Test
    public void add_multiple_orders_successful() {
        testMultipleAddOrders(BID);
        testMultipleAddOrders(OFFER);
    }

    private void testMultipleAddOrders(char side) {
        Order order1 = newOrder(side, 10.0);
        Order order2 = newOrder(side, 20.0);
        Order order3 = newOrder(side, 30.0);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        sleep(1000);
        List<Order> orders = orderBook.getOrders(side);

        assertThat(orders.stream().filter(it -> it.getId() == order1.getId())
                .count()).isEqualTo(1);
        assertThat(orders.stream().filter(it -> it.getId() == order2.getId())
                .count()).isEqualTo(1);
        assertThat(orders.stream().filter(it -> it.getId() == order3.getId())
                .count()).isEqualTo(1);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            fail("sleep: interrupted due to: "+e.getMessage());
        }
    }

}
