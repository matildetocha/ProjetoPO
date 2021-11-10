package ggc.core;

class SimpleProduct extends Product {
	SimpleProduct(String id) {
		super(id);
		changeRecipe(null);
	}

	@Override
	public String toString() {
		return getId() + "|" + Math.round(getPrice()) + "|" + getQuantity();
	}
}
