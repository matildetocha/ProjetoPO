package ggc.core;

import java.lang.Math;

class SimpleProduct extends Product {
    SimpleProduct(String id) {
        super(id);
    }

    public String toString() {
        return super.getId() + "|" +Math.round(getPrice()) + "|" + checkQuantity();
    }

    double getPrice() {
        double maxPrice = 0;
    
        for (Batch batch : super.getBatches()) {
            if (maxPrice < batch.getPrice())
                maxPrice = batch.getPrice();
        }
        super.changeMaxPrice(maxPrice);
        return super.getMaxPrice();

    }
}
