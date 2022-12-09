package org.vijay.order.data;

public class Order {
    private long id;
    private double price;
    private char side; // B - Bid, O - Offer
    private long size;

    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
