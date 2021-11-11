package ggc.core;

import java.io.Serializable;

public abstract class Notification implements Serializable {
	private static final long serialVersionUID = 202109192006L;

	private DeliveryMethod _type;
	private Product _product;

	Notification(DeliveryMethod type, Product product, double price) {
		_type = type;
		_product = product;
	}

	Product getProduct() {
		return _product;
	}
}