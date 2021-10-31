package ggc.core;

public class SaleByCredit extends Sale {
	private int _deadline;
	private double _amountPaid;

	SaleByCredit(int id, Partner partner, Product product, int paymentDate, int baseValue, int quantity, int deadline) {
		super(id, partner, product, paymentDate, baseValue, quantity);
		_deadline = deadline;
	}

	public String toString() {
		return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + _amountPaid + "|"
				+ _deadline + "|" + getPaymentDate();
	}
}
