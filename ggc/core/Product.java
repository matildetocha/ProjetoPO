package ggc.core;

import java.util.HashSet;

abstract class Product{
    private double _maxPrice;
    private String _id;
    private HashSet<Batch> _batches;

    Product(String id) {
        _id = id;
    }

    abstract void checkQuantity(int quantity, Partner p);

    public String getId(){
        return _id;
    }

    public double getMaxPrice(){
        return _maxPrice;
    }

}
