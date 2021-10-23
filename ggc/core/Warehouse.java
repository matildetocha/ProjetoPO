package ggc.core;

// FIXME import classes (cannot import from pt.tecnico or ggc.app)

import java.io.Serializable;
import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import ggc.core.Partner;
import ggc.core.exception.BadEntryException;
import ggc.core.Acquisition;
import ggc.core.Date;

/**
 * TESTE TESTE no windows Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202109192006L;

	private Date _date;
	private int _nextTransactionId;
	public Map<String, Partner> _partners; // ! mudei para set, porque assumo que não se podem repetir partners

	Warehouse() {
		_partners = new HashMap<>();
	}

	Map<String, Partner> getPartners() {
		return _partners;
	}

	Partner getPartner(String id) {
		Set<String> keys = getPartners().keySet();
		Iterator<String> iterator = keys.iterator();
		Partner partner;

		while (iterator.hasNext()) {
			partner = getPartners().get(iterator.next());
			if (partner.getId().equals(id)) {
				return partner;
			}
		}
		return null;
	}

	void registerPartner(String name, String id,  String address) throws BadEntryException {
		Partner partner = new Partner(name, id, address);
		for (String key : getPartners().keySet()) {
			if (key.equals(partner.getId())) {
				throw new BadEntryException(partner.getId());
			}
		}

		getPartners().put(partner.getId(), partner);
	}

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
		// FIXME implement method
	}

}
