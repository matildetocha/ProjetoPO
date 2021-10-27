package ggc.core;

public class Component {
    private int _quantity;
    private Product _product;

    Component(int quantity, Product product) {
        _quantity = quantity;
        _product = product;
    }
    
    public String toString(){
        return _product.getId() + ":" + _quantity;
    }
   // public type method(type){}
    
}
