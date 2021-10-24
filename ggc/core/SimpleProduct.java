package ggc.core;

class SimpleProduct extends Product{
    SimpleProduct(String id) {
        super(id);
    }

    @Override
    void checkQuantity(int quantity, Partner p) {
    
    }

    public String toString() {
        return super.getId() + "|" + super.getMaxPrice() + "|" + "stock-atual-total";  // ! método para ver o stock-atual-total  
    }
}
