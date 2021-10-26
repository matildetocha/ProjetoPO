package ggc.core;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private double _aggravation;
    private Product _aggregateProduct;
    private List<Component> _components;
    
    Recipe(Product aggregateProduct, double aggravation) {
        _aggregateProduct = aggregateProduct;
    }

    void addComponent(Component component) {
        _components.add(component);
    }


    //public String toString(){}
}
