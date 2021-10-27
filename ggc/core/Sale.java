package ggc.core;

abstract class Sale extends Transaction {
    
    private Product _product;
    private Partner _partner;
    private int _quantity;

    void Sale(Product product, int quantity, Partner partner){
        _product = product;
        _quantity = quantity;
        _partner = partner;
    }

    public String toString(){
        return "|" + _partner.getId() + "|" + _product.getId() + "|" + _quantity + "|";
    }
}
