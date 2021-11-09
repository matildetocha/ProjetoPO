package ggc.core;

class SimpleProduct extends Product {
	SimpleProduct(String id) {
		super(id);
	}

	@Override
	public String toString() {
		return getId() + "|" + Math.round(getPrice()) + "|" + checkQuantity();
	}

	Recipe getRecipe(){
		return null;
	}
}
