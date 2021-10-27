package ggc.core;

import java.lang.Math;

class SimpleProduct extends Product{
    SimpleProduct(String id) {
        super(id);
    }

    @Override
    void checkQuantity(int quantity, Partner p) {
    
    }

    public String toString() {
        return getId() + "|" + Math.round(getPrice()) + "|" + checkQuantity();  // ! método para ver o stock-atual-total  
    }

    public double getPrice(){
		double res = 0;
		for(Batch batch : this.getBatches()){
			res += batch.getPrice();
			if(batch.getPrice() > res)
				res = batch.getPrice();
		}
		return res;
	}
}
