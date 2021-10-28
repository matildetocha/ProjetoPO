package ggc.core;

import java.io.Serializable;

public class Batch implements Serializable {
	private static final long serialVersionUID = 252103192986L;

	private double _price;
	private int _quantity;
	private Product _product;
	private Partner _partner;

	Batch(Product product, Partner partner, double price, int quantity) {
		_product = product;
		_partner = partner;
		_price = price;
		_quantity = quantity;
	}

	double getPrice() {
		return _price;
	}

	int getQuantity() {
		return _quantity;
	}

	Product getProduct() {
		return _product;
	}

	Partner getPartner() {
		return _partner;
	}

	@Override
	public String toString() {
		return _product.getId() + "|" + _partner.getId() + "|" + Math.round(_price) + "|" + _quantity;
	}
	// Batch makeCopy(){}

}
