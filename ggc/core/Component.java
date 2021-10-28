package ggc.core;

import java.io.Serializable;

public class Component implements Serializable {
	private static final long serialVersionUID = 900129164072L;

	private int _quantity;
	private Product _product;

	Component(int quantity, Product product) {
		_quantity = quantity;
		_product = product;
	}

	@Override
	public String toString() {
		return _product.getId() + ":" + _quantity;
	}
	// public type method(type){}
}
