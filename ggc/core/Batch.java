package ggc.core;

import java.io.Serializable;
import java.util.Comparator;

public class Batch implements Serializable {
	private static final long serialVersionUID = 202109192006L;

	/** A Batch has a product's quantity and price defined in it. */
	private double _price;
	private int _quantity;

	/**
	 * A Batch has a product associated whose quantity and price are defined in the
	 * respective Batch. A Product can be present in multiple Batches, with
	 * different quantitys and price.
	 */
	private Product _product;

	/**
	 * A Batch only has access one partner and a partner can have access to multiple
	 * Batches.
	 */
	private Partner _partner;

	/**
	 * Batche's constructor that receives a product, a partner and the product's
	 * price and quantity.
	 * 
	 * @param product  Batche's product
	 * @param partner  Batche's partner
	 * @param price    Batche's price per unit
	 * @param quantity Batche's number of units
	 */
	Batch(Product product, Partner partner, double price, int quantity) {
		_product = product;
		_partner = partner;
		_price = price;
		_quantity = quantity;
	}

	/**
	 * Returns the Batche's product price.
	 * 
	 * @return The Batche's current price
	 */
	double getPrice() {
		return _price;
	}

	/**
	 * Returns the Batche's product quantity.
	 * 
	 * @return The Batche's quantity.
	 */
	int getQuantity() {
		return _quantity;
	}

	void changeQuantity(int quantity) {
		_quantity += quantity;
	}

	/**
	 * Returns the product associated to the Batch.
	 * 
	 * @return The Batche's associated product
	 */
	Product getProduct() {
		return _product;
	}

	/**
	 * Returns the partner associated to the Batch.
	 * 
	 * @return The Batche's associated partner
	 */
	Partner getPartner() {
		return _partner;
	}

	/**
	 * Returns the conversion of the Batche's parametres to string to be displayed
	 * to the user.
	 * 
	 * @return The Batche's description with it's Id, partner, price and quantity in
	 *         a string
	 */
	@Override
	public String toString() {
		return _product.getId() + "|" + _partner.getId() + "|" + Math.round(_price) + "|" + _quantity;
	}

	public static Comparator<Batch> getComparator() {
		return BATCH_COMPARATOR;
	}

	public static Comparator<Batch> getPriceComparator() {
		return BATCH_PRICE_COMPARATOR;
	}

	private static final Comparator<Batch> BATCH_COMPARATOR = new Comparator<Batch>() {
		@Override
		public int compare(Batch a, Batch b) {
			if (a.getProduct().getId().toLowerCase().compareTo(b.getProduct().getId().toLowerCase()) == 0) {
				if (a.getPartner().getId().toLowerCase().compareTo(b.getPartner().getId().toLowerCase()) == 0)
					if (a.getPrice() == (b.getPrice()))
						return a.getQuantity() < b.getQuantity() ? -1 : a.getQuantity() == b.getQuantity() ? 0 : 1;
					else
						return a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1;
				else
					return a.getPartner().getId().toLowerCase().compareTo(b.getPartner().getId().toLowerCase());
			} else
				return a.getProduct().getId().toLowerCase().compareTo(b.getProduct().getId().toLowerCase());
		}
	};

	private static final Comparator<Batch> BATCH_PRICE_COMPARATOR = new Comparator<Batch>() {
		@Override
		public int compare(Batch a, Batch b) {
			return a.getPrice() == b.getPrice() ? 0 : a.getPrice() > b.getPrice() ? 1 : -1;
		}
	};
}
