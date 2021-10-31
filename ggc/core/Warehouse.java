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

	Warehouse() {
		_partners = new HashMap<>();
		_products = new HashMap<>();
		_transactions = new HashMap<>();
		_date = new Date();
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

	void registerBatch(Batch batch) {
		Product product = batch.getProduct();
		product.addBatch(batch);
	}

	int displayDate() {
		return _date.now();
	}

	int advanceDate(int days) throws InvalidDateCoreException {
		return _date.add(days);
	}

	Collection<Transaction> getTransactions() {
		return Collections.unmodifiableCollection(_transactions.values());
	}

	Sale registerSale(int quantity, String productId, String partnerId, int deadline) throws UnavailableProductException{

		Product product = _products.get(productId);
		if(product.checkQuantity() < quantity){
			throw new UnavailableProductException(productId, quantity, product.checkQuantity());
		}
		Partner partner = _partners.get(partnerId);
		
		//calcular base value
		// payment date??? como e q sabemos qnd e q ele paga? hmmm
		// o id seria um static que é igual por todo o programa? visto que é o numero de transacoes 

		Sale sale = new SaleByCredit(1, partner, product, 1, 1, quantity, deadline);
		
		partner.addSale(sale);
		_transactions.put(1, sale);
		//id += 1 ;
		return sale;
	}

	int getAvailableStock(String productId){
		Product product = _products.get(productId);
		return product.checkQuantity();
	}

	void registerAcquisiton(String partnerId, String productId, double price, int quantity){
		
		Product product = _products.get(productId);
		//se produto for desconhecido pedir receita, com Message.requestRecipe(), requestComponent e requestAlpha
		Partner partner = _partners.get(partnerId);

		Transaction acquisition = new Acquisition(1, partner, product, 0, 1, quantity); //deadline de aquisitions é 0

		partner.addAcquisition(acquisition);
		_transactions.put(1, acquisition);
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