package ggc.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Product {
	private double _maxPrice;
	private String _id;
	private List<Batch> _batches;

	Product(String id) {
		_id = id;
		_batches = new ArrayList<>();
	}

	String getId() {
		return _id;
	}

	public double getMaxPrice() {
		return _maxPrice;
	}

	void changeMaxPrice(double maxPrice) {
		_maxPrice = maxPrice;
	}

	public void addBatch(Batch batch) {
		_batches.add(batch);
	}

	List<Batch> getBatches() {
		return _batches;
	}

	public int checkQuantity() {
        int res = 0;
        for (Batch batch : _batches) {
            res += batch.getQuantity();
        }
        return res;
    }

	abstract double getPrice();
}
