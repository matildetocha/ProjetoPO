package ggc.core;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;

public class Partner implements Serializable, Observer {
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
	private List<Notification> _notifications;
	private Map<Integer, Transaction> _transactions;
	private Map<Integer, Transaction> _acquisitions;
	private Map<Integer, Transaction> _sales;
	private Map<Integer, Transaction> _breakdowns;

	Partner(String name, String id, String address) {
		_name = name;
		_id = id;
		_address = address;
		Status _statusType = new NormalStatus();
		_status = _statusType.getName();
		_batches = new ArrayList<>();
		_notifications = new ArrayList<>();
		_transactions = new HashMap<>();
		_sales = new HashMap<>();
		_acquisitions = new HashMap<>();
		_breakdowns = new HashMap<>();
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

	Collection<Transaction> getTransactions() {

		return Collections.unmodifiableCollection(_transactions.values());
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

	void addBreakdown(int id, Transaction transaction) {
		_breakdowns.put(id, transaction);
	}

	void addTransaction(int id, Transaction transaction) {
		_transactions.put(id, transaction);
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
	} // FIXME não sei se é preciso

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

	void changeStatus(Status statusType) {
		_status = statusType.getName();
	} // FIXME acho que não precisamos disto

	double getPoints() {
		return _points;
	}

	void changePoints(double points) {
		_points += points;
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

	List<Notification> getNotifications() {
		return Collections.unmodifiableList(_notifications);
	}

	@Override
	public void update(Notification notification) {
		_notifications.add(notification);
	}

	@Override
	public int hashCode() {
		return _id.toLowerCase().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Product && ((Partner) obj)._id.toLowerCase().equals(_id.toLowerCase());
	}

	@Override
	public String toString() {
		return _id + "|" + _name + "|" + _address + "|" + _status + "|" + Math.round(_points) + "|"
				+ Math.round(_valueAcquisitions) + "|" + Math.round(_valueSales) + "|" + Math.round(_valuePaidSales);
	}
}
