package ggc.core;

import java.util.List;

import ggc.core.Product;

public class Batch {
    private double _price;
    private int _quantity;
    private Product _product; //wtf isto vai ser uma lista de produtos ou nao? e q qnd criamos produtos so é para registrar um e metê-lo no batch, idk 
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
