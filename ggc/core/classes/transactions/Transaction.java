package ggc.core.classes.transactions;

import java.sql.Date;

public abstract class Transaction {

    private int _id;
    private Date _paymentDate;
    private int _baseValue;
    private int _quantity;


    public boolean isPaid(){
        return true;
    }

    public Date getPaymentDate(){
        return _paymentDate;
    }


    
}
