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
    public String toString() {
        return super.getId() + "|" + Math.round(getPrice()) + "|" + checkQuantity() + "|" + getRecipe();
    }
}
