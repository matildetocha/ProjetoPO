package ggc.core.classes; 

import java.lang.Math;

public class Date {
    private int _days;

    public int add(int days){
        return _days;
    }
    public int difference(Date other){
        return Math.abs(other.getTime() - _days);
    }

    public int now(){
        return _days; }
   
    public int getTime(){
        return _days;
    }
    
}
