package ggc.core;

import java.io.Serializable;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

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

	void registerPartner(String name, String id, String address) throws BadEntryException {
		Partner partner = new Partner(name, id, address);
		if (_partners.containsKey(id))
			throw new BadEntryException(partner.getId());

		_partners.put(partner.getId(), partner);
	}

	Map<String, Product> getProducts() {
		return _products;
	}

	Product getProduct(String id) throws BadEntryException {
		if (!_products.containsKey(id))
			throw new BadEntryException(id);

		return _products.get(id);
	}

	void registerProduct(Product product) throws BadEntryException {
		if (_products.containsKey(product.getId()))
			throw new BadEntryException(product.getId());

		_products.put(product.getId(), product);
	}

	List<Batch> getBatchesByPartner(String id) throws BadEntryException {
		if (!_partners.containsKey(id))
			throw new BadEntryException(id);
		return _partners.get(id).getBatches();
	}

	List<Batch> getBatchesByProduct(String id) throws BadEntryException {
		if (!_products.containsKey(id))
			throw new BadEntryException(id);
		return _products.get(id).getBatches();
	}

	List<Batch> getBatches(){	    

		List<Batch> orderedBatches = new ArrayList<Batch>();
		
		Set<String> keys = this.getPartners().keySet();
			Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) 
		_receiver.getPartner(iterator.next());

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
