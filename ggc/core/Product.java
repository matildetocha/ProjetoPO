package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Product implements Serializable {
	private static final long serialVersionUID = 202109192006L;

	/** A Product has a maxPrice defined that can be changed. */
	private double _maxPrice;

	/** A Product has an Id that is used to distinguish it from another Product. */
	private String _id;

	/** A Product can be in multiple batches, that are saved in a List. */
	private List<Batch> _batches;

	/**
	 * Product's constructor that receives an Id and creates a new empty ArrayList
	 * of batches.
	 * 
	 * @param id Product's Id
	 */
	Product(String id) {
		_id = id;
		_batches = new ArrayList<>();
	}

	/**
	 * A Product has a hashcode that makes manipulating and searching for it more
	 * efficient.
	 * 
	 * @return The HashCode of the product's Id
	 */
	@Override
	public int hashCode() {
		return _id.hashCode();
	}

	/**
	 * A product is different to another product if their Id is not the same.
	 * 
	 * @param obj An Object of any Class
	 * @return true if the object received is the same as the Product and false if
	 *         not
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Product && ((Product) obj)._id.equals(_id);
	}

	/**
	 * A product can obtain it's own Id.
	 * 
	 * @return the product's id
	 */
	String getId() {
		return _id;
	}

	/**
	 * A product can get it's max price.
	 * 
	 * @return the product's max price
	 */
	public double getMaxPrice() {
		return _maxPrice;
	}

	/**
	 * A product can add another batch to the list of batches it belongs to.
	 * 
	 * @param batch A new batch with a product associated
	 */
	public void addBatch(Batch batch) {
		_batches.add(batch);
	}

	/**
	 * A product can see all of the batches it is associated to.
	 * 
	 * @return the product's list of batches it is associated with
	 */
	List<Batch> getBatches() {
		return _batches;
	}

	List<Batch> getBatchesToSell(int amount) {
		List<Batch> batchesByLowestPrice = new ArrayList<>();
		List<Batch> batchesToSell = new ArrayList<>();
		int i = amount;

		batchesByLowestPrice
		Collections.sort(batchesToSell, new BatchComparator());
		Iterator<Batch> iter = _batches.iterator();

		while (i > 0) {
			Batch b = iter.next();
			batchesToSell.add(b);
			i-= b.getQuantity();
		}
		
		return batchesToSell;
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the full stock of the product, meaning the sum of the product's stock
	 * in all batches.
	 * 
	 * @return the product's quantity
	 */
	public int checkQuantity() {
		int res = 0;
		for (Batch batch : _batches) {
			res += batch.getQuantity();
		}
		return res;
	}

	double getPriceByFractions(List<Batch> batchesToSell, int amount) {
		double priceByFractions = 0;

		Iterator<Batch> iter = batchesToSell.iterator();
		while (iter.hasNext()) {
			priceByFractions += iter.next().getPrice();
		}

		return priceByFractions;
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the highest price that the product has in a batch.
	 * 
	 * @return the maximum price of the product
	 */
	public double getPrice() {
		double res = 0;
		for (Batch batch : _batches) {
			if (batch.getPrice() > res)
				res = batch.getPrice();
		}
		_maxPrice = res;
		return res;
	}
}
