package ggc.core;

public class Acquisition extends Transaction {

	// void Acquisition(Product p,Partner Part);
	Acquisition(int id, Partner partner, Product product, int paymentDate, int baseValue, int quantity) {
		super(id, partner, product, 0, baseValue, quantity);
	}
	
	public String toString(){
		return "COMPRA|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|"
				+ getQuantity() + "|" + getPaymentDate(); // id|idParceiro|idProduto|quantidade|valor-pago|data-pagamento falta valor pago???? no uml do stor so ta nas sales deve ser
				// preciso uma formula para calcular o preco tipo
	}

}
