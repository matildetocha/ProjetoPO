package ggc.core;

import ggc.core.Partner;
import ggc.core.Product;

public class AggregateProduct extends Product{
    AggregateProduct(String id) {
        super(id);
    }

    @Override
    void checkQuantity(int quantity, Partner p) {

    }

   // public String toString(){}
    
}
