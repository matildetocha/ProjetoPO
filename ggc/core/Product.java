package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class Product implements Serializable {
	private static final long serialVersionUID = 202109192006L;
	
	private double _maxPrice;
	private String _id;
	private List<Batch> _batches;

	Product(String id) {
		_id = id;
		_batches = new ArrayList<>();
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Product && ((Product) obj)._id.equals(_id);
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
