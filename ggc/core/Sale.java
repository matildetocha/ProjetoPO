package ggc.core;

abstract class Sale extends Transaction {

	private Product _product;
	private Partner _partner;
	private int _quantity;

	Sale(int id, Partner partner, Product product, int paymentDate, int baseValue, int quantity) {
		super(id, partner, product, paymentDate, baseValue, quantity);
	}
	
	

}
