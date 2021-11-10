package ggc.core;

public class Acquisition extends Transaction {
	Acquisition(int id, Partner partner, Product product, int paymentDate, double baseValue, int quantity) {
		super(id, partner, product, baseValue, quantity);
		_isPaid = true;
		
	}
	
	public String toString(){
		return "COMPRA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|"
				+ getQuantity() + "|" + "|" + getBaseValue() + "|" + getPaymentDate(); 
	}

}
