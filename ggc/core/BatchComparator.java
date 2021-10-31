package ggc.core;

import java.util.Comparator;

public class BatchComparator implements Comparator<Batch> {
	@Override
	public int compare(Batch a, Batch b) {
		if (a.getProduct().getId().toLowerCase().compareTo(b.getProduct().getId().toLowerCase()) == 0) {
			if (a.getPartner().getId().toLowerCase().compareTo(b.getPartner().getId().toLowerCase()) == 0)
				if (a.getPrice() == (b.getPrice()))
					return a.getQuantity() < b.getQuantity() ? -1 : a.getQuantity() == b.getQuantity() ? 0 : 1;
				else
					return a.getPrice() < b.getPrice() ? -1 : a.getPrice() == b.getPrice() ? 0 : 1;
			else
				return a.getPartner().getId().toLowerCase().compareTo(b.getPartner().getId().toLowerCase());
		} else
			return a.getProduct().getId().toLowerCase().compareTo(b.getProduct().getId().toLowerCase());
	}
}