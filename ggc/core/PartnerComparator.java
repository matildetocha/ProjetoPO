package ggc.core;

import java.util.Comparator;

public class PartnerComparator implements Comparator<Partner> {
    @Override
    public int compare(Partner a, Partner b) {
        return a.getId().compareTo(b.getId());
    }
}
