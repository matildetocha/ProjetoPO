package ggc.core;

import java.io.Serializable;

import ggc.core.exception.InvalidDateCoreException;

public class Date implements Serializable {
	private static final long serialVersionUID = 202109192006L;
	
	private int _days;

	Date(int days) {
		_days = days;
	}

	int add(int days) throws InvalidDateCoreException {
		if (days < 0) {
			throw new InvalidDateCoreException();
		}
		_days += days;
		return _days;
	}

	int difference(Date other) {
		return Math.abs(other.now() - now());
	}

	int now() {
		return _days;
	}
}
