package ggc.core;

public class SaleByCredit extends Sale {
	private int _deadline;
	private double _amountPaid;

	SaleByCredit(int id, Partner partner, Product product, int deadline, double baseValue, int quantity) {
		super(id, partner, product, baseValue, quantity);
		_deadline = deadline;
	}

	public double getAmoutPaid(int n, Partner partner){ // n 3 para simples e 5 para agregados
		if( (_deadline - Date.now()) >= n){
			_amountPaid = _amountPaid * 0.9;
		}
		
		else if(0 <= _deadline - Date.now() < n){

			if(partner.getStatus() == "NORMAL"){
				_amountPaid = _amountPaid;
			}
			else if(partner.getStatus() == "SELECTION"){
				_amountPaid = _amountPaid * 0.95;
			}
			else if(partner.getStatus() == "ELITE"){
				_amountPaid = _amountPaid * 0.9;
			}

			return _amountPaid;
		}
		else if(0 < Date.now() - _deadline <= n){

		}
		else if( Date.now() - _deadline > n){

		}
	}

	public String toString() {
		if(this.isPaid())
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + _amountPaid + "|"
				+ _deadline + "|" + getPaymentDate();
		else	
			return "VENDA|" + getId() + "|" + getId() + "|" + getQuantity() + "|" + getBaseValue() + "|" + _amountPaid + "|"
				+ _deadline;
		
	}

}
