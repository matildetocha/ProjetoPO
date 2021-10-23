package ggc.core;

class SimpleProduct extends Product{
    SimpleProduct(String id) {
        super(id);
    }

    @Override
    void checkQuantity(int quantity, Partner p) {
    
    }
/*
    public String toString() {
        idProduto|preço-máximo|stock-actual-total
    }*/
}
