package ggc.core;

import java.util.HashSet;
import java.util.Set;

abstract class Product{
    private double _maxPrice;
    private String _id;
    private Set<Batch> _batches;

    Product(String id) {
        _id = id;
        _batches = new HashSet<>();
    }

    abstract void checkQuantity(int quantity, Partner p);

    public String getId(){
        return _id;
    }

    public double getMaxPrice(){
        return _maxPrice;
    }

}
