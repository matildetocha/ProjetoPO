package ggc.core;

public abstract class Notification {
    private Notification _type;
    private Product _product;
    private double _price;
    
    Notification(Notification type, Product product, double price) {
        _type = type;
        _product = product;
        _price = price;
    }

    @Override
    public String toString() {
        return _type + "|" + _product.getId() + "|" + _price;
    }
}