package ggc.core;

import java.util.ArrayList;
import java.util.List;

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

	Partner(String name, String id, String address) {
		_name = name;
		_id = id;
		_address = address;
		_status = NORMAL;
		_points = 0;
		_valueAcquisitions = _valueSales = _valuePaidSales = 0;
		_batches = new ArrayList<>();
	}

	String getId() {
		return _id;
	}

	List<Batch> getBatches() {
		return _batches;
	}

	void setStatus() {
		if (_points <= 2000)
			_status = NORMAL;
		else if (_points < 25000 && _points > 2000)
			_status = SELECTION;
		else if (_points > 25000)
			_status = ELITE;
	}

	void addBatch(Batch batch) {
		_batches.add(batch);
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
