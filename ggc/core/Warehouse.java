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
	private static double _globalBalance;

	Warehouse() {
		_date = new Date(0);
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

	int getGlobalBalance() {
		return (int) Math.round(_globalBalance);
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

	int getAvailableStock(String productId) {
		Product product = _products.get(productId.toLowerCase());
		return product.getQuantity();
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

	void createSimpleProduct(String id) throws DuplicateProductCoreException {
		Product product = new SimpleProduct(id);
		registerProduct(product);
	}

	boolean isAggregateProduct(String id) {
		return _products.get(id.toLowerCase()).getRecipe() != null;
	}

	List<Batch> getSortedBatches() {
		List<Batch> orderedBatches = new ArrayList<Batch>();

		Set<String> keys = _products.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			orderedBatches.addAll(_products.get(iterator.next()).getBatches());
		}

		Collections.sort(orderedBatches, new BatchComparator());
		return Collections.unmodifiableList(orderedBatches);
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
				it.remove();
			}
		}

		Collections.sort(orderedBatches, new BatchComparator());
		return orderedBatches;
	}

	List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		if (_partners.get(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		return Collections.unmodifiableList(_partners.get(id.toLowerCase()).getBatches());
	}

	List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		if (_products.get(id.toLowerCase()) == null)
			throw new UnknownProductCoreException();
		return Collections.unmodifiableList(_products.get(id.toLowerCase()).getBatches());
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

		else if (_transactions.get(id) instanceof SaleByCredit) {
			SaleByCredit sale = (SaleByCredit) _transactions.get(id);
			sale.getAmountToPay(displayDate());
		}
		return _transactions.get(id);
	}

	Collection<Transaction> getPayedTransactionsByPartner(String id) throws UnknownUserCoreException {
		if (getPartner(id) != null)
			;
		return Collections.unmodifiableCollection(_partners.get(id.toLowerCase()).getPayedTransactions());
	}

	void registerSaleByCredit(String productId, String partnerId, int deadline, int quantity)
			throws UnavailableProductCoreException {
		Product product = _products.get(productId.toLowerCase());
		Date deadlineDate = new Date(deadline);

		if (product.getQuantity() < quantity) {
			throw new UnavailableProductCoreException();
		}

		Partner partner = _partners.get(partnerId.toLowerCase());
		List<Batch> batchesToSell = product.getBatchesToSell(quantity);

		double baseValue = product.getPriceByFractions(batchesToSell, quantity);

		// vai receber uma nova batch com um preco mais alto possivelmente, e vai ter
		// mais quantiodade
		product.updateMaxPrice(baseValue);
		product.updateQuantity(quantity);

		_nextTransactionId++;

		Transaction sale = new SaleByCredit(_nextTransactionId, partner, product, baseValue, quantity, deadlineDate);

		partner.addSale(_nextTransactionId, sale);
		partner.changeValueSales(baseValue);
		changeGlobalBalance(baseValue); // FIXME Changeglobalbalance para mudar mais tarde talvez

		_transactions.put(_nextTransactionId, sale);

		product.updateBatchStock(batchesToSell, quantity);
	}

	void saleAggProduct(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, DuplicateProductCoreException, UnknownProductCoreException {
		AggregateProduct product = (AggregateProduct) _products.get(productId.toLowerCase());
		int amountToCreate = quantity - product.getQuantity();

		for (Component c : product.getRecipe().getComponents()) {
			if (c.getQuantity() * amountToCreate < c.getProduct().getQuantity())
				throw new UnavailableProductCoreException();
		}

		List<String> productIds = new ArrayList<>();
		List<Integer> quantities = new ArrayList<>();

		for (Component c : product.getRecipe().getComponents()) {
			productIds.add(c.getProduct().getId());
			quantities.add(c.getQuantity());
		}

		createAggregateProduct(productId, product.getRecipe().getAlpha(), productIds, quantities,
				product.getRecipe().getComponents().size());

		for (Component c : product.getRecipe().getComponents()) {
			Product productToComp = getProduct(c.getProduct().getId());
			List<Batch> batchesToSell = product.getBatchesToSell(c.getQuantity() * amountToCreate);

			// muda a quantidade dos produtos
			productToComp.updateBatchStock(batchesToSell, c.getQuantity() * amountToCreate);
		}
		// cria o produto agregado
		Batch batch = new Batch(_products.get(productId), _partners.get(partnerId),
				product.getRecipe().getPrice(amountToCreate), quantity);
		registerBatch(batch);

		registerSaleByCredit(productId, partnerId, deadline, quantity);
	}

	void registerAcquisition(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException {
		if (_products.get(productId.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		double baseValue = price * quantity;
		// vai receber uma nova batch com um preco mais alto possivelmente, e vai ter
		// mais quantiodade
		product.updateMaxPrice(price);
		product.updateQuantity(quantity);

		_nextTransactionId++;

		Transaction acquisition = new Acquisition(_nextTransactionId, partner, product, 0, baseValue, quantity);

		partner.addAcquisition(_nextTransactionId, acquisition);
		partner.changeValueAcquisitions(baseValue);

		_transactions.put(_nextTransactionId, acquisition);

		Batch batch = new Batch(product, partner, baseValue, quantity);
		registerBatch(batch);
	}

	void registerBreakdown(String partnerId, String productId, int quantity) throws UnavailableProductCoreException {
		// parte 1 - cria transacao regista e consegue as variaveis todas
		if (_products.get(productId.toLowerCase()).getQuantity() < quantity)
			throw new UnavailableProductCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		double baseValue = product.getPrice() * quantity;

		_nextTransactionId++;

		// comecar a ir buscar os componentes PARTE 2

		List<Component> components = product.getRecipe().getComponents();
		double paidValue = 0;
		double price = 0;
		double totalPrice = 0;
		for (Component c : components) { // se nao houver lotes com o componente entao fica o preco mais alto do
																			// historico, se houver entao é o mais baixo

			List<Batch> batchesByLowestPrice = new ArrayList<>(c.getProduct().getBatches());
			Collections.sort(batchesByLowestPrice, new BatchComparator());
			Batch lowestPriceBatch = batchesByLowestPrice.get(0);

			if (lowestPriceBatch != null) {
				price = lowestPriceBatch.getPrice();
			} else { // se nao houver nenhuma lote com o componente vaise ao passado ver qual a
								// transacao mais cara com o produto
				price = c.getProduct().getMaxPriceHistory(getTransactions());
			}

			// if(_products.get(c.getProduct().getId()) == null){ //caso o componente nao
			// esteja registado

			// Product newProduct = new Product(c.getProduct().getId());
			// registerProduct(newProduct);

			// }

			Batch batch = new Batch(c.getProduct(), partner, price, (quantity * c.getQuantity())); // 5 das aguas vezes 2 do
																																															// hidrogenio (assumir que
																																															// o produto ja existe)
			registerBatch(batch);
			price *= (quantity * c.getQuantity());
			totalPrice += price;
		}
		// fim do for

		totalPrice *= (1 + product.getRecipe().getAlpha()); // basicamente somamos os precos de todos os componentes vezes a
																												// sua quantidade e multiplicamos pelo alpha
		double difference = baseValue - totalPrice; // existe a diferenca entre a compra (o preco do produto agregado vezes
																								// quantidade)
		// e a venda( soma dos precos dos componentes vezes o alpha)
		if (difference < 0) {
			partner.changeValueSales(0);
			paidValue = 0;
		} else {
			partner.changeValueSales(difference);
			changeGlobalBalance(difference);
			paidValue = difference;
		}

		Transaction breakdown = new BreakdownSale(_nextTransactionId, product, quantity, partner, difference, paidValue);

		partner.addBreakdown(_nextTransactionId, breakdown);
		_transactions.put(_nextTransactionId, breakdown);

	}

	public void payTransaction(int transactionId) throws UnknownTransactionCoreException {
		if (_transactions.get(transactionId) == null)
			throw new UnknownTransactionCoreException();

		Date currentDate = new Date(0);
		SaleByCredit unpaidTransaction = (SaleByCredit) _transactions.get(transactionId);
		Date deadline = unpaidTransaction.getDeadline();
		Partner partner = unpaidTransaction.getPartner();
		Product product = unpaidTransaction.getProduct();
		double price = unpaidTransaction.getBaseValue();
		Status status = partner.getStatusType();
		int n;

		if (product.getRecipe() == null) {
			n = 3;
		} else
			n = 5;

		price = partner.getAmountToPay(currentDate, deadline, price, n);

		unpaidTransaction.pay();
		partner.changeValuePaidSales(price);
		partner.changePoints(status.getPoints(partner, currentDate, deadline, price, n));
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