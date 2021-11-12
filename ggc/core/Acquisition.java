package ggc.core;

public class Acquisition extends Transaction {
	Acquisition(int id, Partner partner, Product product, Date paymentDate, double baseValue, int quantity) {
		super(id, partner, product, baseValue, quantity);
		_isPaid = true;
		setPaymentDate(paymentDate);
	}

	public String toString() {
		return "COMPRA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getQuantity() + "|"
				+ Math.round(getBaseValue()) + "|" + getPaymentDate().now();
	}
}
