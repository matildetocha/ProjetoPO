package ggc.core;

public class AggregateProduct extends Product {
    AggregateProduct(String id, Recipe recipe) {
        super(id);
        changeRecipe(recipe);
    }

    @Override
    public String toString() {
        return super.getId() + "|" + Math.round(getMaxPrice()) + "|" + getQuantity() + "|" + getRecipe();
    }
}
