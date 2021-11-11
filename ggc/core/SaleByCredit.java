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

	void setAmountPaid(double amountPaid){
		_amountPaid = amountPaid;
	}

	void getAmountToPay(int days) {
		Partner partner = getPartner();
		Date currentDate = new Date(days);
		Product product = getProduct();
		int n = 0;

		if (product.getRecipe() == null)
			n = 5;

		else
			n = 3;
		_amountPaid = partner.getStatusType().getAmountToPay(currentDate, _deadline, getBaseValue(), n);
	}

	@Override
	public String toString() {
		if (isPaid())
			return "VENDA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getQuantity() + "|"
					+ Math.round(getBaseValue()) + "|" + Math.round(_amountPaid) + "|" + _deadline.now() + "|" + getPaymentDate().now();
		else
			return "VENDA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getQuantity() + "|"
					+ Math.round(getBaseValue()) + "|" + Math.round(_amountPaid) + "|" + _deadline.now();
	}
}
