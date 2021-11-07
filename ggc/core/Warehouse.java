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
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownTransactionCoreException;
import ggc.core.exception.BadEntryException;

import ggc.app.exception.UnavailableProductException; // ! não podemos dar import destas exceções

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
	private Map<Integer, Transaction> _transactions;
	private List<Batch> _batches;

	Warehouse() {
		_partners = new HashMap<>();
		_products = new HashMap<>();
		_transactions = new HashMap<>();
		_date = new Date();
		_batches = new ArrayList<>();
	}

	int displayDate() {
		return _date.now();
	}

	int advanceDate(int days) throws InvalidDateCoreException {
		return _date.add(days);
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

	Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		if (_transactions.get(id) == null)
			throw new UnknownTransactionCoreException();

		return _transactions.get(id);
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

	Collection<Transaction> getTransactions() {
		return Collections.unmodifiableCollection(_transactions.values());
	}
	List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		if (_partners.get(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		return _partners.get(id).getBatches();
	}

	List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		if (_products.get(id.toLowerCase()) == null)
			throw new UnknownProductCoreException();
		return _products.get(id).getBatches();
	}

	void registerProduct(Product product) throws DuplicateProductCoreException {
		if (_products.get(product.getId().toLowerCase()) != null)
			throw new DuplicateProductCoreException();

		_products.put(product.getId().toLowerCase(), product);
	}

	void registerBatch(Batch batch) {
		Product product = batch.getProduct();
		product.addBatch(batch);
	}

	Sale registerSale(int quantity, String productId, String partnerId, int deadline) throws UnavailableProductException {

		Product product = _products.get(productId);
		if (product.checkQuantity() < quantity) {
			throw new UnavailableProductException(productId, quantity, product.checkQuantity());
		}
		Partner partner = _partners.get(partnerId);

		// calcular base value
		double baseValue = product.getPrice() * quantity;
		// payment date??? como e q sabemos qnd e q ele paga? hmmm
		// o num de transacoes aumenta

		Transaction._id += 1;

		Sale sale = new SaleByCredit(Transaction._id, partner, product, deadline, baseValue, quantity);
		// payment date é uma variavel que depois fica definida

		partner.addSale(sale);
		_transactions.put(Transaction._id, sale);

		return sale;
	}

	void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		if (_partners.get(id.toLowerCase()) != null)
			throw new DuplicatePartnerCoreException();

		Partner partner = new Partner(name, id, address);
		_partners.put(partner.getId().toLowerCase(), partner);
	}

    public void registerAggProductId(String productId, Double alpha, List<String> productIds, List<Integer> quantitys, int numComponents) {

		Recipe recipe = new Recipe(alpha);
		for (int i = 0; i < numComponents; i++) {
		
			String prodId = productIds.get(i);
			Integer componentQuantity = quantitys.get(i);

			Component component = new Component(componentQuantity, _products.get(prodId));
			recipe.addComponent(component);

		}
		AggregateProduct product = new AggregateProduct(productId, recipe);
		_products.put(product.getId().toLowerCase(), product);
    }

	void registerSimpleProductId(String productId) {

		Product product = new SimpleProduct(productId);
		_products.put(product.getId().toLowerCase(), product);
	}

	void registerAcquisiton(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException {

		// se produto for desconhecido pedir receita, com Message.requestRecipe(),
		// requestComponent e requestAlpha

		int verifier = 0;

		if (_products.get(productId) == null)
			throw new UnknownProductCoreException();

		Product product = _products.get(productId);

		Partner partner = _partners.get(partnerId);

		double baseValue = product.getPrice() * quantity;
		Transaction._id += 1;

		Transaction acquisition = new Acquisition(Transaction._id, partner, product, 0, baseValue, quantity);																																																		// é 0

		partner.addAcquisition(acquisition);
		_transactions.put(Transaction._id, acquisition);

		Iterator<Batch> it = _batches.iterator();

		while(it.hasNext()){
			if(it.next().getProduct().equals(product)){
				((Batch) it).changeQuantity(quantity);
				verifier++;
			}
		}
		if(verifier == 0)
			registerWarehouseBatch(product, partner, price, quantity);
		
	}

	void registerWarehouseBatch(Product product, Partner partner, double price, int quantity){
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