package ggc.core;

public class BreakdownSale extends Sale {
	private double _paidValue;

	BreakdownSale(int id, Product product, int quantity, Partner partner, double baseValue, double paidValue) {
		super(id, partner, product, baseValue, quantity);
		_paidValue = paidValue;
	}
	public String toString() {
		return "DESAGREGAÇÃO|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|"
				 + getPaymentDate();
	}

	double getPaidValue(){
		return _paidValue;
	}
}
