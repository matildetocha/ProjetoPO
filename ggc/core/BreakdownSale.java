package ggc.core;

public class BreakdownSale extends Sale {

	BreakdownSale(Product product, int quantity, Partner partner) {
		super(1, partner, product, 1, 1, quantity);
	}
	public String toString() {
		return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|"
				 + getPaymentDate();
	}
}
