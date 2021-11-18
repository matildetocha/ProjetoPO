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
import ggc.core.exception.AlreadyPaidTransactionCoreException;
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
	double getAccountingBalance() {
		return Math.round(_accountingBalance + getUnpaidTransactionsBalance());
	}

	double getAvailableBalance() {
		return Math.round(_availableBalance);
	}

	/*
	 * Partners management of the Warehouse
	 */

	List<Partner> getSortedPartners() {
		List<Partner> partnersByKey = new ArrayList<Partner>(_partners.values());
		Collections.sort(partnersByKey, Partner.getComparator());

		return Collections.unmodifiableList(partnersByKey);
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

		Set<String> keys = _products.keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) {
			_products.get(iterator.next()).addObserver(_partners.get(partner.getId().toLowerCase()));
		}
	}

	Collection<Transaction> getPartnerAcquistions(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null) {
			throw new UnknownUserCoreException();
		}
		return _partners.get(partnerId.toLowerCase()).getAcquistions();
	}

	List<Transaction> getPartnerSales(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null) {
			throw new UnknownUserCoreException();
		}

		Partner partner = _partners.get(partnerId.toLowerCase());
		for (Transaction sale : partner.getSalesByCredit()) {
			((SaleByCredit) sale).getAmountToPay(_date.now());
		}

		List<Transaction> sales = new ArrayList<>();
		sales.addAll(partner.getSalesByCredit());
		sales.addAll(partner.getBreakdownSales());

		Collections.sort(sales, Transaction.getComparator());
		return Collections.unmodifiableList(sales);
	}

	/*
	 * Product management of the Warehouse
	 */
	Collection<Product> getProducts() {
		return Collections.unmodifiableCollection(_products.values());
	}

	List<Product> getSortedProducts() {
		List<Product> productsByKey = new ArrayList<Product>(_products.values());
		Collections.sort(productsByKey, Product.getComparator());

		return Collections.unmodifiableList(productsByKey);
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

		while (iterator.hasNext())
			orderedBatches.addAll(_products.get(iterator.next()).getBatches());

		Collections.sort(orderedBatches, Batch.getComparator());
		return Collections.unmodifiableList(orderedBatches);
	}

	List<Batch> getSortedBatchesUnderLimit(double priceLimit) {
		List<Batch> orderedBatches = new ArrayList<Batch>();

		for (Product product : getProducts()) {
			if (product.getQuantity() != 0)
				orderedBatches.addAll(product.getBatches());
		}

		Iterator<Batch> iterator = orderedBatches.iterator();
		while (iterator.hasNext()) {
			Batch batch = iterator.next();
			if (batch.getPrice() >= priceLimit || batch.getQuantity() == 0)
				iterator.remove();
		}

		if (!orderedBatches.isEmpty())
			Collections.sort(orderedBatches, Batch.getComparator());
		return Collections.unmodifiableList(orderedBatches);
	}

	List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		if (_partners.get(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		List<Batch> batchesByPartner = new ArrayList<>();
		Iterator<Batch> iterator = _partners.get(id.toLowerCase()).getBatches().iterator();

		while (iterator.hasNext()) {
			Batch batch = iterator.next();
			if (batch.getQuantity() != 0)
				batchesByPartner.add(batch);
		}

		Collections.sort(batchesByPartner, Batch.getComparator());
		return Collections.unmodifiableList(batchesByPartner);
	}

	List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		if (_products.get(id.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		List<Batch> batchesByProduct = new ArrayList<Batch>(_products.get(id.toLowerCase()).getBatches());

		Collections.sort(batchesByProduct, Batch.getComparator());
		return Collections.unmodifiableList(batchesByProduct);
	}

	void registerBatch(Batch batch) {
		Product product = batch.getProduct();
		Partner partner = batch.getPartner();

		product.addBatch(batch);
		partner.addBatch(batch);
	}

	void removeBatch(Batch batch) {
		Product product = batch.getProduct();
		Partner partner = batch.getPartner();

		product.removeBatch(batch);
		partner.removeBatch(batch);
	}

	void removeBatchCopy(Batch batch) {
		Product product = batch.getProduct();
		List<Batch> copyBatches = new ArrayList<Batch>(product.copyBatches());

		copyBatches.remove(batch);
	}

	void updateBatchStock(List<Batch> batchesToSell, int amount) {
		int i, lastI = batchesToSell.size() - 1;

		if (batchesToSell.size() != 1) {
			for (i = 0; i < batchesToSell.size() - 1; i++) {
				amount -= batchesToSell.get(i).getQuantity();
				removeBatch(batchesToSell.get(i));
			}
		}

		batchesToSell.get(lastI).changeQuantity(-amount);
		if (batchesToSell.get(lastI).getQuantity() == 0)
			removeBatch(batchesToSell.get(lastI));
	}

	void updateBatchStockCopy(List<Batch> batchesToSell, int amount) {
		int i, lastI = batchesToSell.size() - 1;

		if (batchesToSell.size() != 1) {
			for (i = 0; i < batchesToSell.size() - 1; i++) {
				amount -= batchesToSell.get(i).getQuantity();
				removeBatchCopy(batchesToSell.get(i));
			}
		}

		batchesToSell.get(lastI).changeQuantity(-amount);
		if (batchesToSell.get(lastI).getQuantity() == 0)
			removeBatchCopy(batchesToSell.get(lastI));
	}

	/*
	 * Transaction management of the Warehouse
	 */

	double getUnpaidTransactionsBalance() {
		double res = 0;
		for (Transaction transaction : getTransactions()) {
			if (!transaction.isPaid()) {
				if (transaction instanceof SaleByCredit) {
					((SaleByCredit) transaction).getAmountToPay(_date.now());
					res += ((SaleByCredit) transaction).getAmountPaid();
				}
			}
		}
		return res;
	}

	Collection<Transaction> getTransactions() {
		return Collections.unmodifiableCollection(_transactions.values());
	}

	Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		if (_transactions.get(id) == null)
			throw new UnknownTransactionCoreException();

		if (_transactions.get(id) instanceof SaleByCredit) {
			if (!_transactions.get(id).isPaid()) {
				SaleByCredit sale = (SaleByCredit) _transactions.get(id);
				sale.getAmountToPay(_date.now());
			}
		}
		return _transactions.get(id);
	}

	Collection<Transaction> getPaidTransactionsByPartner(String id) throws UnknownUserCoreException {
		if (getPartner(id.toLowerCase()) == null)
			throw new UnknownUserCoreException();
		return Collections.unmodifiableCollection(_partners.get(id.toLowerCase()).getPaidTransactions());
	}

	void registerSaleByCredit(String productId, String partnerId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownUserCoreException, UnknownProductCoreException {
		if (_products.get(productId.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		Product product = _products.get(productId.toLowerCase());
		Date deadlineDate = new Date(deadline);

		if (product.getQuantity() < quantity)
			throw new UnavailableProductCoreException();

		Partner partner = _partners.get(partnerId.toLowerCase());
		List<Batch> batchesToSell = product.getBatchesToSell(quantity);

		double baseValue = product.getPriceByFractions(batchesToSell, quantity);

		Transaction sale = new SaleByCredit(_nextTransactionId, partner, product, baseValue, quantity, deadlineDate);

		partner.addSale(_nextTransactionId, sale);
		partner.addTransaction(_nextTransactionId, sale);
		partner.changeValueSales(baseValue);

		_transactions.put(_nextTransactionId, sale);
		_nextTransactionId++;

		updateBatchStock(batchesToSell, quantity);
	}

	void saleAggProduct(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownUserCoreException, UnknownProductCoreException {
		AggregateProduct product = (AggregateProduct) _products.get(productId.toLowerCase());
		int amountToCreate = quantity - product.getQuantity();

		tryToAggregate(partnerId, productId, deadline, quantity);

		List<Batch> batchesToSell = new ArrayList<>();

		for (Component c : product.getRecipe().getComponents())
			batchesToSell.addAll(c.getProduct().getBatchesToSell(amountToCreate * c.getQuantity()));

		if (batchesToSell.size() == product.getRecipe().getComponents().size()) {
			Batch batch = new Batch(_products.get(productId.toLowerCase()), _partners.get(partnerId.toLowerCase()),
					product.getRecipe().getPrice(amountToCreate), amountToCreate);
			registerBatch(batch);
		}

		else {
			List<Double> prices = new ArrayList<>();
			int i = amountToCreate;
			while (i > 0) {
				prices.add(product.getRecipe().getPrice(amountToCreate));
				for (Component c : product.getRecipe().getComponents())
					updateBatchStock(c.getProduct().getBatchesToSell(c.getQuantity()), c.getQuantity());
			
				i--;
			}

			for (double price : prices) {
				Batch batch = new Batch(_products.get(productId.toLowerCase()), _partners.get(partnerId.toLowerCase()), price,
						1);
				registerBatch(batch);
			}
		}

		product.updateMaxPrice();

		registerSaleByCredit(productId, partnerId, deadline, quantity);
	}

	void tryToAggregate(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownProductCoreException {
		AggregateProduct product = (AggregateProduct) _products.get(productId.toLowerCase());
		int amountToCreate = quantity - product.getQuantity();

		for (Component c : product.getRecipe().getComponents()) {
			if (c.getQuantity() * amountToCreate > c.getProduct().getQuantity()) {
				if (!isAggregateProduct(c.getProduct().getId()))
					throw new UnavailableProductCoreException(c.getProduct().getId(), c.getProduct().getQuantity(),
							c.getQuantity() * amountToCreate);
				else
					tryToAggregate(partnerId, c.getProduct().getId(), deadline, c.getQuantity() * amountToCreate);
			}

			else {
				List<Batch> batchesToSell = c.getProduct().getBatchesToSell(c.getQuantity() * amountToCreate);
				updateBatchStockCopy(batchesToSell, c.getQuantity() * amountToCreate);
			}
		}
	}

	void registerAcquisition(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException, UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		else if (_products.get(productId.toLowerCase()) == null)
			throw new UnknownProductCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		double baseValue = price * quantity;

		Date copyDate = new Date(_date.now());

		Transaction acquisition = new Acquisition(_nextTransactionId, partner, product, copyDate, baseValue, quantity);

		acquisition.setPaymentDate(copyDate);

		partner.addAcquisition(_nextTransactionId, acquisition);
		partner.addTransaction(_nextTransactionId, acquisition);
		partner.changeValueAcquisitions(baseValue);

		_transactions.put(_nextTransactionId, acquisition);
		_nextTransactionId++;

		if (product.getQuantity() == 0 && product.getMaxPrice() != 0)
			sendNewNotification(product, price);

		else if (product.getQuantity() != 0 && price < product.getMinPrice())
			sendBargainNotification(product, price);

		Batch batch = new Batch(product, partner, price, quantity);
		registerBatch(batch);

		product.updateMaxPrice();

		_availableBalance -= baseValue;
		_accountingBalance -= baseValue;
	}

	void registerBreakdown(String partnerId, String productId, int quantity) throws UnavailableProductCoreException {
		if (_products.get(productId.toLowerCase()).getQuantity() < quantity)
			throw new UnavailableProductCoreException();

		Product product = _products.get(productId.toLowerCase());
		Partner partner = _partners.get(partnerId.toLowerCase());
		List<Batch> batchesToBreakdown = product.getBatchesToSell(quantity);
		double baseValue = product.getPriceByFractions(batchesToBreakdown, quantity);
		double paidValue;
		List<Component> components = product.getRecipe().getComponents();
		double totalPrice = getTotalRecipePrice(components, partner, quantity);
		double difference = baseValue - totalPrice;

		updateBatchStock(batchesToBreakdown, quantity);

		if (difference < 0) {
			partner.changeValueSales(0);
			paidValue = 0;
		}

		else {
			partner.changeValueSales(0);
			paidValue = difference;
		}

		_accountingBalance += paidValue;
		_availableBalance += paidValue;

		Transaction breakdown = new BreakdownSale(_nextTransactionId, product, quantity, partner, difference, paidValue);
		((BreakdownSale) breakdown).setRecipe(product.getRecipe());
		Date currentDate = new Date(_date.now());
		breakdown.setPaymentDate(currentDate);

		partner.addBreakdown(_nextTransactionId, breakdown);
		partner.addTransaction(_nextTransactionId, breakdown);
		partner.getStatusType().changePoints(partner, currentDate, currentDate, difference, 0);

		_transactions.put(_nextTransactionId, breakdown);
		_nextTransactionId++;
	}

	double getTotalRecipePrice(List<Component> components, Partner partner, int quantity) {
		double price;
		double totalPrice = 0;

		for (Component c : components) {
			List<Batch> batchesByLowestPrice = new ArrayList<>(c.getProduct().getBatches());
			Collections.sort(batchesByLowestPrice, Batch.getPriceComparator());

			if (batchesByLowestPrice.isEmpty()) {
				price = c.getProduct().getMaxPrice();
				c.setBreakdownPrice(price);
			}

			else {
				price = batchesByLowestPrice.get(0).getPrice();
				c.setBreakdownPrice(price);
			}

			Batch batch = new Batch(c.getProduct(), partner, price, quantity * c.getQuantity());

			registerBatch(batch);
			price *= (quantity * c.getQuantity());
			totalPrice += price;
		}
		return totalPrice;
	}

	public void payTransaction(int transactionId)
			throws UnknownTransactionCoreException, AlreadyPaidTransactionCoreException {
		if (_transactions.get(transactionId) == null)
			throw new UnknownTransactionCoreException();

		else if (_transactions.get(transactionId).isPaid() || !(_transactions.get(transactionId) instanceof SaleByCredit))
			throw new AlreadyPaidTransactionCoreException();

		SaleByCredit unpaidTransaction = (SaleByCredit) _transactions.get(transactionId);
		Date deadline = unpaidTransaction.getDeadline();
		Date currentDate = new Date(_date.now());
		Partner partner = unpaidTransaction.getPartner();
		Product product = unpaidTransaction.getProduct();
		double price = unpaidTransaction.getBaseValue();
		Status status = partner.getStatusType();

		unpaidTransaction.setPaymentDate(currentDate);
		int n;

		if (product.getRecipe() == null)
			n = 5;

		else
			n = 3;

		price = partner.getAmountToPay(currentDate, deadline, price, n);

		_availableBalance += price;
		_accountingBalance += price;

		unpaidTransaction.pay();
		unpaidTransaction.setAmountPaid(price);
		partner.changeValuePaidSales(price);
		status.changePoints(partner, currentDate, deadline, price, n);
	}

	/*
	 * Notification management of the Warehouse
	 */

	List<Notification> getNotifications(String partnerId) throws UnknownUserCoreException {
		if (_partners.get(partnerId.toLowerCase()) == null)
			throw new UnknownUserCoreException();

		return Collections.unmodifiableList(_partners.get(partnerId.toLowerCase()).getNotifications());
	}

	void sendNewNotification(Product product, double price) {
		DeliveryMethod type = new DefaultMethod();
		type.sendNewNotification(product, price);
	}

	void sendBargainNotification(Product product, double price) {
		DeliveryMethod type = new DefaultMethod();
		type.sendBargainNotification(product, price);
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

		if (product.getObserver(partner))
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