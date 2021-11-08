package ggc.core;

public class NormalStatus implements Status{
  
  public double getAmountPaid(Date deadline, double price, int n){
    int i;
    if( (_deadline - Date.now()) >= n){

		return price * 0.9;
		}
		
	else if(0 <= _deadline - Date.now() < n){

	    return price;
		}
	else if(0 < Date.now() - _deadline <= n){
        for(i = 0; i < (Date.now() - _deadline); i++){
            price = price * 1.05;
        }

        return price;
		}
	else if( Date.now() - _deadline > n){
        for(i = 0; i < (Date.now() - _deadline); i++){
            price = price * 1.1;
        }

        return price;
		}

  }

}