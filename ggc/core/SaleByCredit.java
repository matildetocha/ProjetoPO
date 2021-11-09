package ggc.core;

public class SaleByCredit extends Sale {
	private Date _deadline;
	private double _amountPaid;

	SaleByCredit(int id, Partner partner, Product product, double baseValue, int quantity, Date deadline) {
		super(id, partner, product, baseValue, quantity);
		_deadline = deadline;
	}

	Date getDeadline() {
		return _deadline;
	}

	double getAmountPaid() {
		return _amountPaid;
	}

	public String toString() {
		if(this.isPaid())
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + getAmountPaid() + "|"
				+ getDeadline() + "|" + getPaymentDate();
		else	
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + getAmountPaid() + "|"
				+ getDeadline();
	}
}
