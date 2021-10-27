package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private static final long serialVersionUID = 202109192006L;
    
    private double _aggravation;
    private Product _aggregateProduct;
    private List<Component> _components;
    
    Recipe(double aggravation) {
        _aggravation = aggravation;
    }

    void addAggregateProduct(Product aggregateProduct) {
        _aggregateProduct = aggregateProduct;
    }

    void addComponent(Component component) {
        _components.add(component);
    }

    public String toString(){
        String res = "";
        for(Component component : _components){
            res += component.toString();
        }
        return res;
    }

    //public String toString(){}
}
