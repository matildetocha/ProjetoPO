package ggc.core;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;

public class Partner implements Serializable {
	private static final long serialVersionUID = 202109192006L;

	private String _id;
	private String _name;
	private String _address;
	private String _status;
	private double _points;
	private double _valueAcquisitions;
	private double _valueSales;
	private double _valuePaidSales;
	private List<Batch> _batches;
	private Map<Integer, Transaction> _transactions;
	private Map<Integer, Transaction> _acquisitions;
	private Map<Integer, Transaction> _sales;

	Partner(String name, String id, String address) {
		_name = name;
		_id = id;
		_address = address;
		Status _statusType = new NormalStatus();
		_status = _statusType.getName();
		_points = 0;
		_valueAcquisitions = _valueSales = _valuePaidSales = 0;
		_batches = new ArrayList<>();
		_transactions = new HashMap<>();
		_sales = new HashMap<>();
		_acquisitions = new HashMap<>();
	}

	String getId() {
		return _id;
	}

	List<Batch> getBatches() {
		return _batches;
	}

	Collection<Transaction> getPayedTransactions() {
		Map<Integer, Transaction> _payedTransactions = new HashMap<>();

		for (Transaction t : _transactions.values()) {
			if (t.isPaid())
				_payedTransactions.put(t.getId(), t);
		}

		return Collections.unmodifiableCollection(_payedTransactions.values());
	}

	void addBatch(Batch batch) {
		_batches.add(batch);
	}

	Collection<Transaction> getSales() {
		return Collections.unmodifiableCollection(_sales.values());
	}

	void addSale(int id, Transaction transaction) {
		_sales.put(id, transaction);
	}

	Collection<Transaction> getAcquistions() {
		return Collections.unmodifiableCollection(_acquisitions.values());
	}

	void addAcquisition(int id, Transaction transaction) {
		_acquisitions.put(id, transaction);
	}

	void setStatus() {
		Status statusNormal = new NormalStatus();
		Status statusSelection = new SelectionStatus();
		Status statusElite = new EliteStatus();

		if (_points <= 2000)
			_status = statusNormal.getName();

		else if (_points < 25000 && _points > 2000)
			_status = statusSelection.getName();

		else if (_points > 25000)
			_status = statusElite.getName();
	}

	String getStatus() {
		return _status;
	}

	Status getStatusType() {
		Status statusNormal = new NormalStatus();
		Status statusSelection = new SelectionStatus();
		Status statusElite = new EliteStatus();

		if (_status.equals(statusNormal.getName()))
			return statusNormal;

		else if (_status.equals(statusSelection.getName()))
			return statusNormal;

		else 
			return statusElite;
	}

	void changeStatus(Status statusType){
		_status = statusType.getName();
	}

	double getPoints() {
		return _points;
	}

	void changeValueAcquisitions(double price) {
		_valueAcquisitions += price;
	}

	void changeValueSales(double price) {
		_valueSales += price;
	}

	void changeValuePaidSales(double price) {
		_valuePaidSales += price;
	}

	double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
		return getStatusType().getAmountToPay(currentDate, deadline, price, n);
	}

	void changePoints(double points) {
		_points += points;
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
