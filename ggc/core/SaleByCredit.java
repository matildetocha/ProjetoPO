package ggc.core;

public class SaleByCredit extends Sale {
	private Date _Date;
	private double _amountPaid;

	SaleByCredit(Product product, int quantity, Partner partner) {
		super(product, quantity, partner);
	}

	// public String toString(){}
}
