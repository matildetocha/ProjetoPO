package ggc.core;

import java.util.Comparator;

public class BatchComparator implements Comparator<Batch> {
    @Override
    public int compare(Batch a, Batch b) {
        if (a.getProduct().getId().compareTo(b.getProduct().getId()) == 0) {
            if (a.getPartner().getId().compareTo(b.getPartner().getId()) == 0)
                return a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1;
            else 
                return a.getPartner().getId().compareTo(b.getPartner().getId());
        }
        else 
            return a.getProduct().getId().compareTo(b.getProduct().getId());
    }
}