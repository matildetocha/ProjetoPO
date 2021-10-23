package ggc.core.classes.products;

abstract class Product{
    
    private double _maxPrice;
    private String _id;

    Product(String id) {
    }

   // abstract void checkQuantity(int quantity, Partner p);
    public String getId(){
        return _id;
    }

    public double getMaxPrice(){
        return _maxPrice;
    }

}
