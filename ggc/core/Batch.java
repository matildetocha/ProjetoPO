package ggc.core;

public class Batch {
    private double _price;
    private int _quantity;
    private Product _product;
    private Partner _partner;

    Batch(Product product, Partner partner, double price, int quantity) {
        _product = product;
        _partner = partner;
        _price = price;
        _quantity = quantity;
    }

    public String toString(){
        return _partner.getId() + "|" + _product.getId() + "|" + _price + "|" + _quantity;
    }

    //Batch makeCopy(){}
    
}
