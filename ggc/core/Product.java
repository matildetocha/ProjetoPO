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

	abstract void checkQuantity(int quantity, Partner p);

	String getId() {
		return _id;
	}

	double getMaxPrice() {
		return _maxPrice;
	}

	void addBatch(Batch batch) {
		_batches.add(batch);
	}

}
