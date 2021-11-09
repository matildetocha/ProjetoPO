package ggc.core;

abstract class Sale extends Transaction {
	Sale(int id, Partner partner, Product product, double baseValue, int quantity) {
		super(id, partner, product, baseValue, quantity);
	}
}
