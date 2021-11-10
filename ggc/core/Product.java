package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
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

	private Recipe _recipe;

	private int _quantity;

	private List<Observer> _observers;

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
		Collections.sort(batchesByLowestPrice, new BatchComparator());
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

	int getQuantity2() {
		return _quantity;
	}

	void updateQuantity(int quantity) {
		_quantity += quantity;
	}

	void updateMaxPrice(double price) {
		if (price > _maxPrice)
			_maxPrice = price;
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

	// procura em todas as transacoes pelo produto que quer, ve o preco e vai buscar
	// o mais alto
	double getMaxPriceHistory(Collection<Transaction> transactions) {
		double res = 0;
		Iterator<Transaction> iterator = transactions.iterator();
		while (iterator.hasNext()) {
			Transaction transaction = iterator.next();
			if (transaction.getProduct().equals(this)) {
				if (transaction.getBaseValue() > res)
					res = transaction.getBaseValue();
			}
		}
		_maxPrice = res;
		return res;
	}

	void updateBatchStock(List<Batch> batchesToSell, int amount) {
		int i;

		for (i = 0; i < batchesToSell.size(); i++) {
			batchesToSell.get(i).changeQuantity(-(batchesToSell.get(i).getQuantity()));
			// altera a quantidade do produto ja que subtraimos na batch
			batchesToSell.get(i).getProduct().updateQuantity(-(batchesToSell.get(i).getQuantity()));
			if (batchesToSell.get(i).getQuantity() == 0)
				_batches.remove(batchesToSell.get(i));
		}
	}

	/**
	 * By checking every single batch that is associated to the product, the method
	 * returns the highest price that the product has in a batch.
	 * 
	 * @return the maximum price of the product
	 */
	double getPrice() {
		double res = 0;
		for (Batch batch : _batches) {
			if (batch.getPrice() > res)
				res = batch.getPrice();
		}
		_maxPrice = res;
		return res;
	}

	Recipe getRecipe() {
		return _recipe;
	}

	void changeRecipe(Recipe recipe) {
		_recipe = recipe;
	}

	// void notifyAllObservers() {
	// 	for (Observer observer : _observers) {
	// 		observer.update();
	// 	}
	// }
}
