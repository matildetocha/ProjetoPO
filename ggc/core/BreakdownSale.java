package ggc.core;

public class BreakdownSale extends Sale {

	BreakdownSale(int id, Product product, int quantity, Partner partner, double baseValue) {
		super(id, partner, product, baseValue, quantity);
	}
	public String toString() {
		return "DESAGREGAÇÃO|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|"
				 + getPaymentDate();
	}
}
