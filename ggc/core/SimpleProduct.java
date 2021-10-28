package ggc.core;

class SimpleProduct extends Product {
	SimpleProduct(String id) {
		super(id);
	}

	double getPrice() {
		double maxPrice = 0;

		for (Batch batch : getBatches()) {
			if (maxPrice < batch.getPrice())
				maxPrice = batch.getPrice();
		}
		changeMaxPrice(maxPrice);
		return getMaxPrice();
	}

	@Override
	public String toString() {
		return getId() + "|" + Math.round(getPrice()) + "|" + checkQuantity();
	}
}
