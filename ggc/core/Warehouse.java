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
	private static double _accountingBalance;
	private static double _availableBalance;

	Warehouse() {
		_date = new Date(0);
		_partners = new HashMap<>();
		_products = new HashMap<>();
		_transactions = new HashMap<>();
	}

	/*
	 * Time management of the Warehouse.
	 */
	int displayDate() {
		return _date.now();
	}

	int advanceDate(int days) throws InvalidDateCoreException {
		return _date.add(days);
	}

	/*
	 * Currency management of the Warehouse
	 */
	int getAccountingBalance() {
		return (int) Math.round(_accountingBalance + getUnpaidTransactionsBalance());
	}

	int getAvailableBalance() {
		return (int) Math.round(_availableBalance);
	}

	/*
	 * Partners management of the Warehouse
	 */

	Collection<Partner> getPartners() {
		return Collections.unmodifiableCollection(_partners.values());
	}

	List<Partner> getSortedPartners() {
		List<Partner> partnersByKey = new ArrayList<Partner>(_partners.values());
		Collections.sort(partnersByKey, new PartnerComparator());

		return partnersByKey;
	}

	Partner getPartner(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		return _partners.get(partnerId.toLowerCase());
	}

	void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		if (_partners.get(id.toLowerCase()) != null)
			throw new DuplicatePartnerCoreException();

		Partner partner = new Partner(name, id, address);
		_partners.put(partner.getId().toLowerCase(), partner);
	}

	Collection<Transaction> getPartnerAcquistions(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null) {
			throw new UnknownUserCoreException();
		}
		return _partners.get(partnerId.toLowerCase()).getAcquistions();
	}

	Collection<Transaction> getPartnerSalesByCredit(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null) {
			throw new UnknownUserCoreException();
		}

		Partner partner = _partners.get(partnerId.toLowerCase());
		for (Transaction sale : partner.getSalesByCredit()) {
			((SaleByCredit) sale).getAmountToPay(_date.now());
		}

		return _partners.get(partnerId.toLowerCase()).getSalesByCredit();
	}

	Collection<Transaction> getPartnerBreakdownSale(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null) {
			throw new UnknownUserCoreException();
		}

		return _partners.get(partnerId.toLowerCase()).getBreakdownSales();
	}

	/*
	 * Product management of the Warehouse
	 */
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

		Set<String> keys = _partners.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			_products.get(product.getId().toLowerCase()).addObserver(_partners.get(iterator.next()));
		}
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

	int getAvailableStock(String productId) {
		Product product = _products.get(productId.toLowerCase());
		return product.getQuantity();
	}

	/*
	 * Batch management of the Warehouse
	 */

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

		for (Product product : getProducts()) {
			orderedBatches.addAll(product.getBatches());
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

	/*
	 * Transaction management of the Warehouse
	 */

	double getUnpaidTransactionsBalance() {
		double res = 0;
		for (Transaction transaction : getTransactions()) {
			if (!transaction.isPaid()) {
				((SaleByCredit) transaction).getAmountToPay(_date.now());
				res += ((SaleByCredit) transaction).getAmountPaid();
			}
		}
		return res;
	}

	Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		if (_transactions.get(id) == null)
			throw new UnknownTransactionCoreException();

		if (_transactions.get(id) instanceof SaleByCredit) {
			SaleByCredit sale = (SaleByCredit) _transactions.get(id);
			sale.getAmountToPay(_date.now());
		}
		return _transactions.get(id);
	}

	Collection<Transaction> getPayedTransactionsByPartner(String id) throws UnknownUserCoreException {
		if (getPartner(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		return Collections.unmodifiableCollection(_partners.get(id.toLowerCase()).getPayedTransactions());
	}

	void registerSaleByCredit(String productId, String partnerId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownUserCoreException {
		if (_products.get(productId) == null)
			throw new UnknownUserCoreException(); //FIXME FAZER O OUTRO GET MAS DO PARTNER

		Product product = _products.get(productId.toLowerCase());
		Date deadlineDate = new Date(deadline);

		System.out.println(product);
		if (product.getQuantity() < quantity) {
			throw new UnavailableProductCoreException();
		}

		Partner partner = _partners.get(partnerId.toLowerCase());
		List<Batch> batchesToSell = product.getBatchesToSell(quantity);

		double baseValue = product.getPriceByFractions(batchesToSell, quantity);

		Transaction sale = new SaleByCredit(_nextTransactionId, partner, product, baseValue, quantity, deadlineDate);
		partner.addSale(_nextTransactionId, sale);
		partner.addTransaction(_nextTransactionId, sale);

		partner.changeValueSales(baseValue);

		_transactions.put(_nextTransactionId, sale);
		_nextTransactionId++;

		product.updateBatchStock(batchesToSell, quantity);
		product.updateQuantity(quantity);
	}

	void saleAggProduct(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, DuplicateProductCoreException, UnknownProductCoreException {
		AggregateProduct product = (AggregateProduct) _products.get(productId.toLowerCase());
		int amountToCreate = quantity - product.getQuantity();

		for (Component c : product.getRecipe().getComponents()) {
			if (c.getQuantity() * amountToCreate > c.getProduct().getQuantity())
				throw new UnavailableProductCoreException(c.getProduct().getId(), c.getProduct().getQuantity(),
						c.getQuantity() * amountToCreate);
		} // !!! teste 19.06 ele nao esta a dar um erro errado e dps ja nao tem as
			// transacoes

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

			productToComp.updateBatchStock(batchesToSell, c.getQuantity() * amountToCreate);
		}

		Batch batch = new Batch(_products.get(productId), _partners.get(partnerId),
				product.getRecipe().getPrice(amountToCreate), quantity);
		registerBatch(batch);

		registerSaleByCredit(productId, partnerId, deadline, quantity);
	}

	void registerAcquisition(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException {
		if (_products.get(productId.toLowerCase()) == null) {
			throw new UnknownProductCoreException();
		}

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		double baseValue = price * quantity;

		Transaction acquisition = new Acquisition(_nextTransactionId, partner, product, 0, baseValue, quantity);

		Date copyDate = new Date(_date.now());
		acquisition.setPaymentDate(copyDate);

		partner.addAcquisition(_nextTransactionId, acquisition);
		partner.addTransaction(_nextTransactionId, acquisition);
		partner.changeValueAcquisitions(baseValue);
		partner.setStatus();

		_transactions.put(_nextTransactionId, acquisition);
		_nextTransactionId++;

		if (product.getQuantity() == 0 && product.getMaxPrice() != 0)
			sendNewNotification(product);

		else if (product.getQuantity() != 0 && price < product.getMinPrice())
			sendBargainNotification(product);

		Batch batch = new Batch(product, partner, price, quantity);
		registerBatch(batch);

		product.updateMaxPrice();
		product.updateQuantity(quantity);

		_availableBalance -= baseValue;
		_accountingBalance -= baseValue;
	}

	void registerBreakdown(String partnerId, String productId, int quantity) throws UnavailableProductCoreException {
		if (_products.get(productId.toLowerCase()).getQuantity() < quantity)
			throw new UnavailableProductCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		double baseValue = product.getMaxPrice() * quantity;
		double paidValue = 0;

		List<Component> components = product.getRecipe().getComponents();

		double totalPrice = getTotalRecipePrice(components, partner, quantity);

		double difference = baseValue - totalPrice;

		if (difference < 0) {
			partner.changeValueSales(0);
			paidValue = 0;
		} else {
			partner.changeValueSales(difference);
			paidValue = difference;
		}

		Transaction breakdown = new BreakdownSale(_nextTransactionId, product, quantity, partner, difference, paidValue);
		((BreakdownSale) breakdown).setRecipe(product.getRecipe());
		breakdown.setPaymentDate(_date);

		partner.addBreakdown(_nextTransactionId, breakdown);
		partner.addTransaction(_nextTransactionId, breakdown);
		partner.changePoints(partner.getStatusType().getPoints(partner, _date, _date, difference, 0));

		_transactions.put(_nextTransactionId, breakdown);
		_nextTransactionId++;
	}

	double getTotalRecipePrice(List<Component> components, Partner partner, int quantity) {

		double price = 0;
		double totalPrice = 0;
		for (Component c : components) {

			List<Batch> batchesByLowestPrice = new ArrayList<>(c.getProduct().getBatches());
			Collections.sort(batchesByLowestPrice, new BatchComparator());
			Batch lowestPriceBatch = batchesByLowestPrice.get(0);

			if (lowestPriceBatch != null) {
				price = lowestPriceBatch.getPrice();
			} else {
				price = c.getProduct().getMaxPrice();
			}
			Batch batch = new Batch(c.getProduct(), partner, price, quantity * c.getQuantity());

			registerBatch(batch);
			price *= (quantity * c.getQuantity());
			totalPrice += price;
		}
		return totalPrice;
	}

	public void payTransaction(int transactionId) throws UnknownTransactionCoreException {
		if (_transactions.get(transactionId) == null)
			throw new UnknownTransactionCoreException();

		SaleByCredit unpaidTransaction = (SaleByCredit) _transactions.get(transactionId);
		Date deadline = unpaidTransaction.getDeadline();
		Partner partner = unpaidTransaction.getPartner();
		Product product = unpaidTransaction.getProduct();
		double price = unpaidTransaction.getBaseValue();
		Status status = partner.getStatusType();

		unpaidTransaction.setPaymentDate(_date);
		int n;

		if (product.getRecipe() == null) {
			n = 5;
		} else
			n = 3;

		price = partner.getAmountToPay(_date, deadline, price, n);

		_availableBalance += price;
		_accountingBalance += price;

		unpaidTransaction.pay();
		unpaidTransaction.setAmountPaid(price);
		partner.changeValuePaidSales(price);
		partner.changePoints(status.getPoints(partner, _date, deadline, price, n));
		partner.setStatus();
	}

	/*
	 * Notification management of the Warehouse
	 */

	List<Notification> getNotifications(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		return Collections.unmodifiableList(_partners.get(partnerId.toLowerCase()).getNotifications());
	}

	Collection<Transaction> getTransactions() {
		return Collections.unmodifiableCollection(_transactions.values());
	}

	void sendNewNotification(Product product) {
		DeliveryMethod type = new DefaultMethod();
		type.sendNewNotification(product);
	}

	void sendBargainNotification(Product product) {
		DeliveryMethod type = new DefaultMethod();
		type.sendBargainNotification(product);
	}

	void clearAllNotifications(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		_partners.get(partnerId.toLowerCase()).clear();
	}

	void toogleProductNotifications(String partnerId, String productId)
			throws UnknownUserCoreException, UnknownProductCoreException {
		if (_products.get(productId.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		else if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());

		if (product.getObservers().contains(partner))
			product.removeObserver(partner);

		else
			product.addObserver(partner);
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