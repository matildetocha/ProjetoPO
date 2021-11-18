package ggc.core;

import java.util.List;

public class BreakdownSale extends Sale {
	private double _paidValue;
	private Recipe _recipe;

	BreakdownSale(int id, Product product, int quantity, Partner partner, double baseValue, double paidValue) {
		super(id, partner, product, baseValue, quantity);
		_paidValue = paidValue;

	}

	public String toString() {
		return "DESAGREGAÇÃO|" + getId() + "|" + getPartner().getId() + "|" + getProduct().getId() + "|" + getQuantity()
				+ "|" + Math.round(getBaseValue()) + "|" + Math.round(getPaidValue()) + "|" + getPaymentDate().now() + "|"
				+ breakdownRecipe();
	}

	double getPaidValue() {
		return _paidValue;
	}

	void setRecipe(Recipe recipe) {
		_recipe = recipe;
	}

	String breakdownRecipe() {
		String string = "";
		List<Component> components = _recipe.getComponents();

		for (Component component : components) {
			string += component.getProduct().getId() + ":" + (component.getQuantity() * getQuantity()) + ":"
					+ Math.round(component.getBreakdownPrice() * getQuantity() * component.getQuantity()) + "#";
		}

		string = string.substring(0, string.length() - 1);
		return string;
	}
}
