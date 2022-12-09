package org.vijay.order;

import org.vijay.order.data.Order;
import org.vijay.order.data.OrderLevel;
import org.vijay.order.exception.InvalidOrderException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class OrderBook {
    public static final char BID = 'B';
    public static final char OFFER = 'O';

    public Map<Long, Order> bidOrders = new HashMap<>();
    public Map<Long, Order> offerOrders = new HashMap<>();

    private final Set<OrderLevel> orderLevels = new TreeSet<>();
    public void addOrder(Order order) {
        validate(order);
        if (order.getSide() == BID)
            addOrder(order, bidOrders);
        else
            addOrder(order, offerOrders);
        calculateOrder();
    }

    public Order remove(long orderId) {
        Long bidOrderId = bidOrders.keySet().stream().filter(it -> it.equals(orderId)).findAny().orElse(-1L);
        Order order;
        if (bidOrderId > 0)
            order = bidOrders.remove(orderId);
        else
            order = offerOrders.remove(orderId);
        if (order != null)
            calculateOrder();
        return order;
    }

    public void update(long orderId, long newSize) {
        if (newSize <= 0)
            throw new InvalidOrderException("Zero size");
        Long bidOrderId = bidOrders.keySet().stream().filter(it -> it.equals(orderId)).findAny().orElse(-1L);
        try {
            if (bidOrderId > 0)
                bidOrders.get(orderId).setSize(newSize);
            else
                offerOrders.get(orderId).setSize(newSize);
            calculateOrder();
        } catch (NullPointerException e) {
            // Order Id not found.
        }
    }

    public double getPrice(char side, int level) {
        OrderLevel orderLevel = orderLevels.stream().findFirst().get();
        if (side == BID) {
            return orderLevel.getBidAt(level).getPrice();
        } else {
            return orderLevel.getOfferAt(level).getPrice();
        }
    }

    public long getSize(char side, int level) {
        OrderLevel orderLevel = orderLevels.stream().findFirst().get();
        if (side == BID) {
            return orderLevel.getBidAt(level).getSize();
        } else {
            return orderLevel.getOfferAt(level).getSize();
        }
    }

    public List<Order> getOrders(char side) {
        Optional<OrderLevel> optional = orderLevels.stream().findFirst();
        if (optional.isEmpty())
            return Collections.emptyList();
        OrderLevel orderLevel = optional.get();
        if (side == BID) {
            return orderLevel.getBids();
        } else {
            return orderLevel.getOffers();
        }
    }

    private void addOrder(Order order, Map<Long, Order> orders) {
        orders.putIfAbsent(order.getId(), order);
    }

    private void validate(Order order) {
        if (order.getSize() <= 0)
            throw new InvalidOrderException("Zero size");
        if ( !(order.getSide() == BID || order.getSide() == OFFER) )
            throw new InvalidOrderException("Incorrect Side");
        if (order.getPrice() < 0)
            throw new InvalidOrderException("Negative price not accepted yet.");
    }

    private void calculateOrder() {
        CompletableFuture.runAsync(this::processOrders);
    }

    private void processOrders() {
        CompletableFuture<List<Order>> sortedBids = CompletableFuture.supplyAsync(
                () -> getSortedOrder(bidOrders.values(), false));
        CompletableFuture<List<Order>> sortedOffers = CompletableFuture.supplyAsync(
                () -> getSortedOrder(offerOrders.values(), true));
        OrderLevel level = new OrderLevel();
        try {
            level.setBids(sortedBids.get());
            level.setOffers(sortedOffers.get());
            orderLevels.add(level);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Order> getSortedOrder(Collection<Order> orders, boolean reverse) {
        return orders.stream().sorted((o1, o2) -> {
                    Double p1 = o1.getPrice();
                    Double p2 = o2.getPrice();
                    return reverse ? p2.compareTo(p1) : p1.compareTo(p2);
                }).collect(Collectors.toList());
    }
}
