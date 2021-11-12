package ggc.core;

import java.io.Serializable;

public abstract class Transaction implements Serializable{
	private static final long serialVersionUID = 202109192006L;

	private int _id;
	private Date _paymentDate;
	private double _baseValue;
	private int _quantity;
	private Partner _partner;
	private Product _product;
	protected boolean _isPaid;

	Transaction(int id, Partner partner, Product product, double baseValue, int quantity) {
		_id = id;
		_partner = partner;
		_product = product;
		_baseValue = baseValue;
		_quantity = quantity;
		_isPaid = false;
	}

	boolean isPaid() {
		return _isPaid;
	}

	void pay() {
		_isPaid = true;
	}

	Date getPaymentDate() {
		return _paymentDate;
	}

	void setPaymentDate(Date date){
		_paymentDate = date;
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

	@Override
	public int hashCode() {
		return _id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Transaction && ((Transaction) obj)._id == _id;
	}
}
