package ggc.core;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {
	@Override
	public int compare(Product a, Product b) {
		return a.getId().compareTo(b.getId());
	}
}