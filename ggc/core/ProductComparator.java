package ggc.core;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        return a.getId().equals(b.getId()) ? -1 : a.getId() == b.getId() ? 0 : 1;
    }
}