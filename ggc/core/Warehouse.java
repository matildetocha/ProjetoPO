package ggc.core;

import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.HashMap;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownUserCoreException;
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
		_products = new HashMap<>();
	}

	Map<String, Partner> getPartners() {
		return _partners;
	}

	Partner getPartner(String id) throws UnknownUserCoreException {
		if (!_partners.containsKey(id.toLowerCase()))
			throw new UnknownUserCoreException();
		return _partners.get(id.toLowerCase());
	}

	void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		Partner partner = new Partner(name, id, address);
		if (_partners.containsKey(id.toLowerCase()))
			throw new DuplicatePartnerCoreException();

		_partners.put(partner.getId().toLowerCase(), partner);
	}

	Map<String, Product> getProducts() {
		return _products;
	}

	Product getProduct(String id) throws UnknownProductCoreException {
		if (!_products.containsKey(id))
			throw new UnknownProductCoreException();

		return _products.get(id);
	}

	void registerProduct(Product product) throws UnknownProductCoreException {
		if (_products.containsValue(product))
			throw new UnknownProductCoreException();

		_products.put(product.getId().toLowerCase(), product);
	}

	void registerBatch(Batch batch) {
		Product product = batch.getProduct();
		product.addBatch(batch);
	}

	List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		if (!_partners.containsKey(id))
			throw new UnknownUserCoreException();
		return _partners.get(id).getBatches();
	}

	List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		if (!_products.containsKey(id))
			throw new UnknownProductCoreException();
		return _products.get(id).getBatches();
	}

	List<Batch> getSortedBatches() {
		List<Batch> orderedBatches = new ArrayList<Batch>();

		Set<String> keys = _partners.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			orderedBatches.addAll(_partners.get(iterator.next()).getBatches());
		}
		Collections.sort(orderedBatches, new BatchComparator());
		return orderedBatches;
	}

	List<Partner> getSortedPartners() {
		List<Partner> partnersByKey = new ArrayList<Partner>(_partners.values());
		Collections.sort(partnersByKey, new PartnerComparator());

		return partnersByKey;
	}

	List<Product> getSortedProducts() {
		List<Product> productsByKey = new ArrayList<Product>(_products.values());
		Collections.sort(productsByKey, new ProductComparator());

		return productsByKey;
	}

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	void importFile(String txtfile) throws IOException, BadEntryException { /* FIXME maybe other exceptions */
		Parser parser = new Parser(this);
		parser.parseFile(txtfile);
	}
}