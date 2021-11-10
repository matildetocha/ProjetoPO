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

	// double getAmountPaid() {
	// return _amountPaid;
	// }

	void getAmountToPay(int days) {
		Partner partner = getPartner();
		Date currentDate = new Date(days);
		Product product = getProduct();
		int n = 0;

		if (product.getRecipe() == null) n = 3;

		else n = 5;
		_amountPaid = partner.getStatusType().getAmountToPay(currentDate, _deadline, getBaseValue(), n);
	}

	public String toString() {
		if (isPaid())
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + _amountPaid + "|"
					+ _deadline + "|" + getPaymentDate();
		else
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + _amountPaid + "|"
					+ _deadline;
	}
}
