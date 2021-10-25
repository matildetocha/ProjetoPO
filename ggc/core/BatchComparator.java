package ggc.core;

import java.util.Comparator;

public class BatchComparator implements Comparator<Batch> {


    @Override
    public int compare(Batch a, Batch b) {
        return a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1;
    }
}