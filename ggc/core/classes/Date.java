package ggc.core.classes; 

import java.lang.Math;

public class Date {
    private static int _days;

    public static void add(int days){
        _days += days;
    }
    public int difference(Date other){
        return Math.abs(other.now() - _days);
    }

    public int now(){
        return _days; }

}
