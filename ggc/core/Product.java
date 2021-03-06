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

	void removeBatch(Batch batch) {
		_batches.remove(batch);
	}

	/**
	 * A product can see all of the batches it is associated to.
	 * 
	 * @return the product's list of batches it is associated with
	 */
	List<Batch> getBatches() {
		return Collections.unmodifiableList(_batches);
	}

	void updateBatches(List<Batch> batches) {
		_batches = batches;
	}

	List<Batch> getBatchesToSell(int amount, List<Batch> batches) {
		List<Batch> batchesByLowestPrice = new ArrayList<Batch>(batches);
		List<Batch> batchesToSell = new ArrayList<>();
		int i = amount;

		Collections.sort(batchesByLowestPrice, Batch.getPriceComparator());
		Iterator<Batch> iter = batchesByLowestPrice.iterator();

		while (i > 0 && iter.hasNext()) {
			Batch b = iter.next();
			if (b.getProduct().equals(this)) {
				batchesToSell.add(b);
				i -= b.getQuantity();
			}
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

	double getPriceByFractions(List<Batch> batchesToSell, int amount) {
		double priceByFractions = 0;
		double lastBatchPrice = batchesToSell.get(batchesToSell.size() - 1).getPrice();

		for (int i = 0; i < batchesToSell.size() - 1; i++) {
			priceByFractions += batchesToSell.get(i).getPrice() * batchesToSell.get(i).getQuantity();
			amount -= batchesToSell.get(i).getQuantity();
		}

		priceByFractions += amount * lastBatchPrice;

		return priceByFractions;
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the highest price that the product has in a batch.
	 * 
	 * @return the maximum price of the product
	 */
	void updateMaxPrice() {
		double res = _maxPrice;

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

	Recipe getRecipe() {
		return _recipe;
	}

	void changeRecipe(Recipe recipe) {
		_recipe = recipe;
	}

	List<Partner> getObservers() {
		return Collections.unmodifiableList(_observers);
	}

	boolean getObserver(Partner partner) {
		return _observers.contains(partner);
	}

	void addObserver(Partner partner) {
		_observers.add(partner);
	}

	void removeObserver(Partner partner) {
		_observers.remove(partner);
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
