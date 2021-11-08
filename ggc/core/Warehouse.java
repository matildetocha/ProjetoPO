package ggc.core;

import java.io.Serializable;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.InvalidDateCoreException;
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownTransactionCoreException;
import ggc.core.exception.BadEntryException;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

	/** Serial number for serialization. */
	private static final long serialVersionUID = 202109192006L;

	private Date _date;
	private int _nextTransactionId;
	private Map<String, Partner> _partners;
	private Map<String, Product> _products;
	private Map<Integer, Transaction> _transactions;
	private List<Batch> _batches;
	private static double _globalBalance;

	Warehouse() {
		_date = new Date();
		_partners = new HashMap<>();
		_products = new HashMap<>();
		_transactions = new HashMap<>();
		_globalBalance = 0;
	}

	int displayDate() {
		return _date.now();
	}

	int advanceDate(int days) throws InvalidDateCoreException {
		return _date.add(days);
	}

	double getGlobalBalance() {
		return _globalBalance;
	}

	void changeGlobalBalance(double price) {
		_globalBalance += price;
	}

	Collection<Partner> getPartners() {
		return Collections.unmodifiableCollection(_partners.values());
	}

	List<Partner> getSortedPartners() {
		List<Partner> partnersByKey = new ArrayList<Partner>(_partners.values());
		Collections.sort(partnersByKey, new PartnerComparator());

		return partnersByKey;
	}

	Partner getPartner(String id) throws UnknownUserCoreException {
		if (_partners.get(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		return _partners.get(id.toLowerCase());
	}

	void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		if (_partners.get(id.toLowerCase()) != null)
			throw new DuplicatePartnerCoreException();

		Partner partner = new Partner(name, id, address);
		_partners.put(partner.getId().toLowerCase(), partner);
	}

	double getBaseValue(Batch batch) {
		return batch.getPrice();
	}

	int getAvailableStock(String productId) {
		Product product = _products.get(productId);
		return product.checkQuantity();
	}

	Collection<Product> getProducts() {
		return Collections.unmodifiableCollection(_products.values());
	}

	List<Product> getSortedProducts() {
		List<Product> productsByKey = new ArrayList<Product>(_products.values());
		Collections.sort(productsByKey, new ProductComparator());

		return productsByKey;
	}

	Product getProduct(String id) throws UnknownProductCoreException {
		if (_products.get(id.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		return _products.get(id.toLowerCase());
	}

	void registerProduct(Product product) throws DuplicateProductCoreException {
		if (_products.get(product.getId().toLowerCase()) != null)
			throw new DuplicateProductCoreException();

		_products.put(product.getId().toLowerCase(), product);
	}

	void createAggregateProduct(String productId, Double alpha, List<String> productIds, List<Integer> quantities,
			int numComponents) throws DuplicateProductCoreException {
		Recipe recipe = new Recipe(alpha);

		for (int i = 0; i < numComponents; i++) {
			String prodId = productIds.get(i);
			Integer componentQuantity = quantities.get(i);

			Component component = new Component(componentQuantity, _products.get(prodId));
			recipe.addComponent(component);
		}

		Product product = new AggregateProduct(productId, recipe);
		registerProduct(product);
	}

	void createSimpleProduct(String productId) throws DuplicateProductCoreException {
		Product product = new SimpleProduct(productId);
		registerProduct(product);
	}

	List<Batch> getSortedBatches() {
		List<Batch> orderedBatches = new ArrayList<Batch>();

		Set<String> keys = _products.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			orderedBatches.addAll(_products.get(iterator.next()).getBatches());
		}

		Collections.sort(orderedBatches, new BatchComparator());
		return orderedBatches;
	}

	List<Batch> getSortedBatchesUnderLimit(double priceLimit) {
		List<Batch> orderedBatches = new ArrayList<Batch>();

		Set<String> keys = _products.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			orderedBatches.addAll(_products.get(iterator.next()).getBatches());
		}

		// remove os com preco menor que o limite
		Iterator<Batch> it = orderedBatches.iterator();
		while (it.hasNext()) {
			if (it.next().getPrice() < priceLimit) {
				orderedBatches.remove(it);
			}
		}

		Collections.sort(orderedBatches, new BatchComparator());
		return orderedBatches;
	}

	List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		if (_partners.get(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		return _partners.get(id).getBatches();
	}

	Collection<Transaction> getPayedTransactionsByPartner(String partnerId) {
		return _partners.get(partnerId).getPayedTransactions();
	}

	List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		if (_products.get(id.toLowerCase()) == null)
			throw new UnknownProductCoreException();
		return _products.get(id).getBatches();
	}

	void registerBatch(Batch batch) {
		Product product = batch.getProduct();
		Partner partner = batch.getPartner();

		product.addBatch(batch);
		partner.addBatch(batch);
	}

	Collection<Transaction> getTransactions() {
		return Collections.unmodifiableCollection(_transactions.values());
	}

	Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		if (_transactions.get(id) == null)
			throw new UnknownTransactionCoreException();

		return _transactions.get(id);
	}

	void registerSale(int amount, String productId, String partnerId, int deadline)
			throws UnavailableProductCoreException {
		Product product = _products.get(productId);

		if (product.checkQuantity() < amount) {
			throw new UnavailableProductCoreException();
		}

		Partner partner = _partners.get(partnerId);
		List<Batch> batchesToSell = product.getBatchesToSell(amount);
		double baseValue = product.getPriceByFractions(batchesToSell, amount);
		_nextTransactionId++;

		Transaction sale = new SaleByCredit(_nextTransactionId, partner, product, deadline, baseValue, amount);

		partner.addSale(_nextTransactionId, sale);
		_transactions.put(_nextTransactionId, sale);
	}

	void registerAcquisiton(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException {
		if (_products.get(productId) == null)
			throw new UnknownProductCoreException();

		Product product = _products.get(productId);
		Partner partner = _partners.get(partnerId);
		double baseValue = product.getPrice() * quantity;

		_nextTransactionId++;

		Transaction acquisition = new Acquisition(_nextTransactionId, partner, product, 0, baseValue, quantity);

		partner.addAcquisition(_nextTransactionId, acquisition);
		_transactions.put(_nextTransactionId, acquisition);

		Batch batch = new Batch(product, partner, baseValue, quantity);
		registerBatch(batch);
	}

	void registerWarehouseBatch(Product product, Partner partner, double price, int quantity) {
		Batch batch = new Batch(product, partner, price, quantity);
		_batches.add(batch);
	}

	void updateBatchQuantity(Batch batch, int quantity) {
		batch.changeQuantity(quantity);
	}

	/**
	 * @param txtfile filename to be loaded.
	 * @throws IOException
	 * @throws BadEntryException
	 */
	void importFile(String txtfile) throws IOException, BadEntryException {
		Parser parser = new Parser(this);
		parser.parseFile(txtfile);
	}

}