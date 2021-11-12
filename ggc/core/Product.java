package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	private Recipe _recipe;

	private int _quantity;

	private List<Partner> _observers;

	/**
	 * Product's constructor that receives an Id and creates a new empty ArrayList
	 * of batches.
	 * 
	 * @param id Product's Id
	 */
	Product(String id) {
		_id = id;
		_batches = new ArrayList<>();
		_observers = new ArrayList<>();
	}

	/**
	 * A Product has a hashcode that makes manipulating and searching for it more
	 * efficient.
	 * 
	 * @return The HashCode of the product's Id
	 */
	@Override
	public int hashCode() {
		return _id.toLowerCase().hashCode();
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
		return obj instanceof Product && ((Product) obj)._id.toLowerCase().equals(_id.toLowerCase());
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
	double getMaxPrice() {
		return _maxPrice;
	}

	/**
	 * A product can add another batch to the list of batches it belongs to.
	 * 
	 * @param batch A new batch with a product associated
	 */
	void addBatch(Batch batch) {
		_batches.add(batch);
	}

	/**
	 * A product can see all of the batches it is associated to.
	 * 
	 * @return the product's list of batches it is associated with
	 */
	List<Batch> getBatches() {
		return Collections.unmodifiableList(_batches);
	}

	List<Batch> getBatchesToSell(int amount) {
		List<Batch> batchesByLowestPrice = new ArrayList<>();
		List<Batch> batchesToSell = new ArrayList<>();
		int i = amount;

		batchesByLowestPrice = _batches;
		Collections.sort(batchesByLowestPrice, Batch.getComparator());
		Iterator<Batch> iter = batchesByLowestPrice.iterator();

		while (i > 0) {
			Batch b = iter.next();
			batchesToSell.add(b);
			i -= b.getQuantity();
		}

		return Collections.unmodifiableList(batchesToSell);
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the full stock of the product, meaning the sum of the product's stock
	 * in all batches.
	 * 
	 * @return the product's quantity
	 */
	int getQuantity() {
		int res = 0;
		for (Batch batch : _batches) {
			res += batch.getQuantity();
		}
		return res;
	}

	void updateQuantity(int quantity) {
		_quantity += quantity;
	}

	double getPriceByFractions(List<Batch> batchesToSell, int amount) {
		double priceByFractions = 0;
		double lastBatchPrice;
		int lastBatchAmount;
		int i;

		lastBatchAmount = batchesToSell.get(batchesToSell.size() - 1).getQuantity();
		lastBatchPrice = batchesToSell.get(batchesToSell.size() - 1).getPrice();

		for (i = 0; i < batchesToSell.size() - 1; i++)
			priceByFractions += batchesToSell.get(i).getPrice() * batchesToSell.get(i).getQuantity();

		priceByFractions += lastBatchAmount * lastBatchPrice - (lastBatchAmount - amount) * lastBatchPrice;

		return priceByFractions;
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the highest price that the product has in a batch.
	 * 
	 * @return the maximum price of the product
	 */
	void updateMaxPrice() {
		double res = 0;

		for (Batch b : _batches) {
			if (b.getPrice() > res)
				res = b.getPrice();
		}
		_maxPrice = res;
	}

	double getMinPrice() {
		double minPrice = _maxPrice;

		for (Batch b : _batches) {
			if (b.getPrice() < minPrice)
				minPrice = b.getPrice();
		}

		return minPrice;
	}

	void updateBatchStock(List<Batch> batchesToSell, int amount) {
		int i, lastI = batchesToSell.size() - 1;

		if (batchesToSell.size() != 1) {
			for (i = 0; i < batchesToSell.size() - 1; i++) {
				batchesToSell.get(i).changeQuantity(-batchesToSell.get(i).getQuantity());
				// altera a quantidade do produto ja que subtraimos na batch
				batchesToSell.get(i).getProduct().updateQuantity(-(batchesToSell.get(i).getQuantity()));
				_batches.remove(batchesToSell.get(i));
				amount -= batchesToSell.get(i).getQuantity();
			}
		}

		batchesToSell.get(lastI).changeQuantity(-amount);
		batchesToSell.get(lastI).getProduct().updateQuantity(-amount);
		if (batchesToSell.get(lastI).getQuantity() == 0)
			_batches.remove(lastI);
	}

	Recipe getRecipe() {
		return _recipe;
	}

	void changeRecipe(Recipe recipe) {
		_recipe = recipe;
	}

	List<Partner> getObservers() {
		return _observers;
	}

	void addObserver(Partner partner) {
		_observers.add(partner);
	}

	void removeObserver(Observer observer) {
		_observers.remove(observer);
	}

	void notifyAllObservers(Notification notification) {
		for (Partner partner : _observers)
			partner.update(notification);
	}

	public static Comparator<Product> getComparator() {
		return PRODUCT_COMPARATOR;
	}

	private static final Comparator<Product> PRODUCT_COMPARATOR = new Comparator<Product>() {
		@Override
		public int compare(Product a, Product b) {
			return a.getId().toLowerCase().compareTo(b.getId().toLowerCase());
		}
	};
}
