package ggc.core;

import java.io.Serializable;
import java.io.IOException;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import ggc.core.exception.BadEntryException;

/**
 * TESTE TESTE no windows Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202109192006L;

	private Date _date;
	private int _nextTransactionId;
	private Map<String, Partner> _partners;
	private Map<String, Product> _products;

	Warehouse() {
		_partners = new HashMap<>();
	}

	Map<String, Partner> getPartners() {
		return _partners;
	}

	Partner getPartner(String id) throws BadEntryException {
		if (!_partners.containsKey(id)) 
			throw new BadEntryException(id);

		return _partners.get(id);
	}

	void registerPartner(String name, String id,  String address) throws BadEntryException {
		Partner partner = new Partner(name, id, address);
			if (_partners.containsKey(id)) 
				throw new BadEntryException(partner.getId());
			
		_partners.put(partner.getId(), partner);
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
