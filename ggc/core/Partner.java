package ggc.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.TransferHandler.TransferSupport;

import java.io.Serializable;

public class Partner implements Serializable {
	private static final long serialVersionUID = 209129194076L;

	private static final String NORMAL = "NORMAL";
	private static final String SELECTION = "SELECTION";
	private static final String ELITE = "ELITE";

	private String _id;
	private String _name;
	private String _address;
	private String _status;
	private double _points;
	private double _valueAcquisitions;
	private double _valueSales;
	private double _valuePaidSales;
	private List<Batch> _batches;
	private List<Transaction> _acquisitions;
	private List<Transaction> _sales;
	private List<Transaction> _transactions;

	Partner(String name, String id, String address) {
		_name = name;
		_id = id;
		_address = address;
		_status = NORMAL;
		_points = 0;
		_valueAcquisitions = _valueSales = _valuePaidSales = 0;
		_batches = new ArrayList<>();
		_sales = new ArrayList<>();
		_acquisitions = new ArrayList<>();
		_transactions = new ArrayList<>();
	}

	String getId() {
		return _id;
	}

	List<Batch> getBatches() {
		return _batches;
	}

	List<Transaction> getPayedTransactions(){
		Iterator<Transaction> iterator = _transactions.iterator();

		List<Transaction> _payedTransactions = new ArrayList<>();

		while(iterator.hasNext()){
			if(iterator.next().isPaid()){
				_payedTransactions.add((Transaction) iterator);
			}
		}
		return _payedTransactions;

	}

	Batch getLowestPriceBatchByProduct(Product product) {
		double lowestPrice = 0;
		Batch lowestPriceBatch;

		for (Batch b: _batches) {
			if (b.getProduct().equals(product) && b.getPrice() <= lowestPrice)
				lowestPrice = b.getPrice();
				lowestPriceBatch = b;
		}
		return lowestPriceBatch;
	}

	void addBatch(Batch batch) {
		_batches.add(batch);
	}

	List<Transaction> getSales() {
		return _sales;
	}

	void addSale(Transaction transaction) {
		_sales.add(transaction);
	}

	public List<Transaction> getAcquistions() {
		return _acquisitions;
	}

	void addAcquisition(Transaction transaction) {
		_acquisitions.add(transaction);
	}

	void addTransaction(Transaction transaction) {
		_transactions.add(transaction);
	}

	void setStatus() {
		if (_points <= 2000)
			_status = NORMAL;
		else if (_points < 25000 && _points > 2000)
			_status = SELECTION;
		else if (_points > 25000)
			_status = ELITE;
	}

	@Override
	public int hashCode() {
		return _id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Product && ((Partner) obj)._id.equals(_id);
	}

	@Override
	public String toString() {
		return _id + "|" + _name + "|" + _address + "|" + _status + "|" + Math.round(_points) + "|"
				+ Math.round(_valueAcquisitions) + "|" + Math.round(_valueSales) + "|" + Math.round(_valuePaidSales);
	}
}
