package org.vijay.order.data;

import java.time.LocalDateTime;
import java.util.List;

public class OrderLevel implements Comparable<OrderLevel> {
    private final LocalDateTime calculatedTime;
    private List<Order> bids;
    private List<Order> offers;

    public OrderLevel() {
        this.calculatedTime = LocalDateTime.now();
    }

    public Order getBidAt(int level) {
        return bids.get(level-1);
    }

    public Order getOfferAt(int level) {
        return offers.get(level-1);
    }

    public List<Order> getBids() {
        return bids;
    }

    public List<Order> getOffers() {
        return offers;
    }

    public void setBids(List<Order> bids) {
        this.bids = bids;
    }

    public void setOffers(List<Order> offers) {
        this.offers = offers;
    }

    @Override
    public int compareTo(OrderLevel o) { // reverse
        return o.calculatedTime.compareTo(calculatedTime);
    }
}
