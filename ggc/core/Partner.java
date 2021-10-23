package ggc.core;

import java.util.HashSet;
import java.util.Set;

import ggc.core.Batch;

public class Partner {
    private String _id;
    private String _name;
    private String _address;
    private String _status;
    private double _points;
    private double _valueAcquisitions;
    private double _valueSales;
    private double _valuePaidSales;
    private Set<Batch> _batches;
        
    Partner(String name, String id, String address) {
        _name = name;
        _id = id;
        _address = address;
        // _status = Normal; // ! fazemos classe enum de status ou deixamos só como constantes, se for o segundo onde colocamos as mesmas???
        _points = 0;
        _valueAcquisitions = _valueSales = _valuePaidSales = 0;
        _batches = new HashSet<>();
    }

    public String getId() {
            return _id;
    }

    public String getName() {
        return _name;
    }

    public String getAddress() {
        return _address;
    }

    public String getStatus() {
        return _status;
    }

    public double getPoints() {
        return _points;
    }

    public double getValueAcquisitions() {
        return _valueAcquisitions;
    }

    public double getValueSales() {
        return _valueSales;
    }

    public double getValuePaidSales() {
        return _valuePaidSales;                     // ? vale a pena fazer gets para tudo??
    }

    public String toString(){

        return _id + "|" + getName() + "|" + getAddress() + "|" + getStatus() + "|" 
            +  getPoints() + "|" + getValueAcquisitions() + "|" + getValueSales() 
            + "|" + getValuePaidSales(); 
    }
}   
