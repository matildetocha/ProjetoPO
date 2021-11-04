package ggc.core;

abstract class Sale extends Transaction {

	private Product _product;
	private Partner _partner;
	private int _quantity;

	Sale(int id, Partner partner, Product product, double baseValue, int quantity) {
		super(id, partner, product, baseValue, quantity);
	}
	
	

}
