package ggc.core;

public class Partner {
    private static final String NORMAL = "Normal";
    private static final String SELECTION = "Selection";
    private static final String ELITE = "Elite";

    private String _id;
    private String _name;
    private String _address;
    private String _status;
    private double _points;
    private double _valueAcquisitions;
    private double _valueSales;
    private double _valuePaidSales;
        
    Partner(String name, String id, String address){
        _name = name;
        _id = id;
        _address = address;
        _status = NORMAL;
        _points = 0;
        _valueAcquisitions = _valueSales = _valuePaidSales = 0;
    }

    String getId(){
        return _id;
    }

    void setStatus() {
        if (_points <= 2000) _status = NORMAL;
        else if (_points < 25000 && _points > 2000) _status = SELECTION;
        else if (_points > 25000) _status = ELITE;

    }

    public String toString(){

        return _id + "|" + _name + "|" + _address + "|" + _status + "|" +  _points + 
            "|" + _valueAcquisitions + "|" + _valueSales + "|" + _valuePaidSales; 
    }
}   
