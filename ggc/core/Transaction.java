package ggc.core;

import java.sql.Date;

public abstract class Transaction {

	private int _id;
	private int _paymentDate;
	private int _baseValue;
	private int _quantity;
	private Partner _partner;
	private Product _product;

	Transaction(int id, Partner partner, Product product, int paymentDate, int baseValue, int quantity) {
		_id = id;
		_partner = partner;
		_product = product;
		_paymentDate = paymentDate;
		_baseValue = baseValue;
		_quantity = quantity;

	}

	public boolean isPaid() {
		return true;
	}

	public int getPaymentDate() {
		return _paymentDate;
	}

	public int getId(){
		return _id;
	}

	public int getBaseValue(){
		return _baseValue;
	}

	public int getQuantity(){
		return _quantity;
	}

	public Partner getPartner(){
		return _partner;
	}
	
	public Product getProduct(){
		return _product;
	}


}
