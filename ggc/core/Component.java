package ggc.core;

import java.io.Serializable;

public class Component implements Serializable {
	private static final long serialVersionUID = 202109192006L;

	private double _breakdownPrice;
	private int _quantity;
	private Product _product;

	Component(int quantity, Product product) {
		_quantity = quantity;
		_product = product;
	}

	void setBreakdownPrice(double breakdownPrice) {
		_breakdownPrice = breakdownPrice;
	}

	double getBreakdownPrice() {
		return _breakdownPrice;
	}

	int getQuantity() {
		return _quantity;
	}

	Product getProduct(){
		return _product;
	}

	@Override
	public String toString() {
		return _product.getId() + ":" + _quantity;
	}
}
