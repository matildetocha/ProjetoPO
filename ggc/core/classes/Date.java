package ggc.core.classes;

public class Date {
    private int _days;

    public int add(int days){
        return _days;
    }
    public int difference(Date other){
        return other._days - _days;
    }

    //static Date now(){
        //return _days; }
   
    public int getTime(){
        return _days;
    }
    
}
