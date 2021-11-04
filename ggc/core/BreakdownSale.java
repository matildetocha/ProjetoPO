package ggc.core;

public class BreakdownSale extends Sale {

	BreakdownSale(Product product, int quantity, Partner partner, double baseValue) {
		super(Transaction._id, partner, product, baseValue, quantity);
	}
	public String toString() {
		return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|"
				 + getPaymentDate();
	}
}
