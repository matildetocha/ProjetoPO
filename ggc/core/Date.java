package ggc.core; 

import java.lang.Math;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.InvalidDateCoreException;
//Adada
public class Date {
    private static int _days;

    public static void add(int days) throws InvalidDateCoreException{
        if(days < 0 ){ 
            throw new InvalidDateCoreException("days");

        }
        _days += days;
    }
    public int difference(Date other){
        return Math.abs(Date.now() - other.now());
    }

    public static int now(){
        return _days; }

}
