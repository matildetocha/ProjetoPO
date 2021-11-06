package ggc.core;

public abstract class Transaction {

	static int _id = 0;
	private int _paymentDate;
	private double _baseValue;
	private int _quantity;
	private Partner _partner;
	private Product _product;
	private boolean _isPaid;

	Transaction(int id, Partner partner, Product product, double baseValue, int quantity) {
		_id = id;
		_partner = partner;
		_product = product;
		_paymentDate = 0; // por ser definida
		_baseValue = baseValue;
		_quantity = quantity;

	}

	public boolean isPaid() {
		return _isPaid;
	}

	public void pay() {
		_isPaid = true;
	}

	public int getPaymentDate() {
		return _paymentDate;
	}

	int getId() {
		return _id;
	}

	double getBaseValue() {
		return _baseValue;
	}

	int getQuantity() {
		return _quantity;
	}

	Partner getPartner() {
		return _partner;
	}

	Product getProduct() {
		return _product;
	}

}
