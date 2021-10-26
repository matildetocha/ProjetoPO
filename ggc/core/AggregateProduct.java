package ggc.core;

public class AggregateProduct extends Product {
    private Recipe _recipe;
    
    AggregateProduct(String id, Recipe recipe) {
        super(id);
        _recipe = recipe;
    }

    Recipe getRecipe() {
        return _recipe;
    }
    
    @Override
    void checkQuantity(int quantity, Partner p) {

    }

    public String toString() {
        return super.getId() + "|" + super.getMaxPrice() + 
            "|" + "stock-atual-total" + "|" + "receita";  // ! método para ver o stock-atual-total  
                                                          // ! método para ver a receita do produto
    }
    
}
