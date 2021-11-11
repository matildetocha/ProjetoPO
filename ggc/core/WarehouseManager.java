package ggc.core;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.InvalidDateCoreException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownTransactionCoreException;
import ggc.core.exception.BadEntryException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

/** Facade for access. */
public class WarehouseManager {

	/** Name of file storing current warehouse. */
	private String _filename = "";

	/** The wharehouse itself. */
	private Warehouse _warehouse = new Warehouse();

	public String getFilename() {
		return _filename;
	}

	public Warehouse getWarehouse() {
		return _warehouse;
	}

	public int getAccountingBalance() {
		return _warehouse.getAccountingBalance();
	}

	public int getAvailableBalance() {
		return _warehouse.getAvailableBalance();
	}

	public int displayDate() {
		return _warehouse.displayDate();
	}

	/**
	 * 
	 * @param days
	 * @return advancedDate
	 * @throws InvalidDateCoreException
	 */
	public int advanceDate(int days) throws InvalidDateCoreException {
		return _warehouse.advanceDate(days);
	}

	public Partner getPartner(String id) throws UnknownUserCoreException {
		return _warehouse.getPartner(id);
	}

	public Collection<Partner> getPartners() {
		return _warehouse.getPartners();
	}

	public void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		_warehouse.registerPartner(name, id, address);
	}

	public Product getProduct(String id) throws UnknownProductCoreException {
		return _warehouse.getProduct(id);
	}

	public Collection<Product> getProducts() {
		return _warehouse.getProducts();
	}

	/**
	 * 
	 * @param product
	 * @throws DuplicateProductCoreException
	 */
	public void registerProduct(Product product) throws DuplicateProductCoreException {
		_warehouse.registerProduct(product);
	}

	public void createAggregateProduct(String productId, Double alpha, List<String> productIds, List<Integer> quantitys,
			int numComponents) throws DuplicateProductCoreException {
		_warehouse.createAggregateProduct(productId, alpha, productIds, quantitys, numComponents);
	}

	public void createSimpleProduct(String id) throws DuplicateProductCoreException {
		_warehouse.createSimpleProduct(id);
	}

	public boolean isAggregateProduct(String id) {
		return _warehouse.isAggregateProduct(id);
	}

	public List<Batch> getSortedBatches() {
		return _warehouse.getSortedBatches();
	}

	public List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		return _warehouse.getBatchesByPartner(id);
	}

	public List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		return _warehouse.getBatchesByProduct(id);
	}

	public void registerBatch(Batch batch) {
		_warehouse.registerBatch(batch);
	}

	public List<Partner> getSortedPartners() {
		return _warehouse.getSortedPartners();
	}

	public List<Product> getSortedProducts() {
		return _warehouse.getSortedProducts();
	}

	// public void registerSale(int quantity, String productId, String partnerId,
	// int deadline)
	// throws UnavailableProductCoreException {
	// _warehouse.registerSale(quantity, productId, partnerId, deadline);
	// }

	public int getAvailableStock(String productId) {
		return _warehouse.getAvailableStock(productId);
	}

	public List<Batch> getSortedBatchesUnderLimit(double priceLimit) {
		return _warehouse.getSortedBatchesUnderLimit(priceLimit);
	}

	public Collection<Transaction> getPayedTransactionsByPartner(String partnerId) throws UnknownUserCoreException {
		return _warehouse.getPayedTransactionsByPartner(partnerId);
	}

	public void registerAcquisition(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException {
		_warehouse.registerAcquisition(partnerId, productId, price, quantity);
	}

	public Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		return _warehouse.getTransaction(id);
	}

	public void registerSaleByCredit(String productId, String partnerId, int deadline, int quantity)
			throws UnavailableProductCoreException {
		_warehouse.registerSaleByCredit(productId, partnerId, deadline, quantity);
	}

	public void saleAggProduct(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, DuplicateProductCoreException, UnknownProductCoreException {
		_warehouse.saleAggProduct(partnerId, productId, deadline, quantity);
	}

	public void registerBreakdown(String partnerId, String productId, int quantity)
			throws UnavailableProductCoreException {
		_warehouse.registerBreakdown(partnerId, productId, quantity);
	}

	public void payTransaction(int transactionId) throws UnknownTransactionCoreException {
		_warehouse.payTransaction(transactionId);
	}

	/**
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 * @@throws MissingFileAssociationException
	 */
	public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
		ObjectOutputStream obOut = null;
		try {
			if (_filename == "") { // if file has no name
				throw new FileNotFoundException();
			}
			ObjectOutputStream dOut = new ObjectOutputStream(new FileOutputStream(_filename));
			obOut = new ObjectOutputStream(dOut);
			obOut.writeObject(_warehouse);
		} finally {
			if (obOut != null)
				obOut.close();
		}
	}

	/**
	 * @@param filename
	 * @@throws MissingFileAssociationException
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 */
	public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
		_filename = filename;
		save();
	}

	/**
	 * @@param filename
	 * @@throws UnavailableFileException
	 */
	public void load(String filename) throws UnavailableFileException, ClassNotFoundException, IOException {
		ObjectInputStream obIn = null;
		try {
			FileInputStream fpin = new FileInputStream(filename);
			ObjectInputStream objIn = new ObjectInputStream(fpin);
			obIn = new ObjectInputStream(objIn);
			Object anObject = obIn.readObject();
			_warehouse = (Warehouse) anObject;
			_filename = filename;

		} finally {
			if (obIn != null)
				obIn.close();
		}
	}

	/**
	 * @param textfile
	 * @throws ImportFileException
	 */
	public void importFile(String textfile) throws ImportFileException {
		try {
			_warehouse.importFile(textfile);
		} catch (IOException | BadEntryException e) {
			throw new ImportFileException(textfile, e);
		}
	}

}
