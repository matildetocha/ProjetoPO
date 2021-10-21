package ggc.core.classes; 

import java.lang.Math;

import ggc.app.exception.InvalidDateException;
import ggc.core.exception.BadEntryException;

public class Date {
    private static int _days;

    public static void add(int days) throws BadEntryException{
        if(days < 0){ 
            throw new BadEntryException("");

        }
        _days += days;
    }
    public int difference(Date other){
        return Math.abs(Date.now() - other.now());
    }

    public static int now(){
        return _days; }

}
