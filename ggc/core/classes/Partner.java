package ggc.core.classes;

public class Partner {
    private String _id;
    private String _name;
    private String _adress;
    private String _status;
    private double _points;
    private double _valueAcquisitions;
    private double _valueSales;
    private double _valuePaidSales;
        
    public Partner(String name, String adress){
        _name = name;
        _adress = adress;
        // _status = Normal;
        _points = 0;
    }

    public String getName(){
        return _name;
    }

    public String getId(){
        return _id;
    }
}
