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

    public double getPrice() {
        double res = 0;
        for (Batch batch : this.getBatches()) {
            res += batch.getPrice();
            if (batch.getPrice() > res)
                res = batch.getPrice();
        }
        return res;
    }

    @Override
    public String toString() {
        return super.getId() + "|" + Math.round(getPrice()) + "|" + checkQuantity() + "|" + getRecipe();
    }
}
