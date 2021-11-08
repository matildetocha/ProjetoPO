package ggc.core;

public class SelectionStatus implements Status{

  public double getAmountPaid(Date deadline, double price, int n){
    int i;
    if( (_deadline - Date.now()) >= n){

		return price * 0.9;
		}
		
	else if(0 <= _deadline - Date.now() < n){
      if(_deadline - Date.now() <= 2)
	      return price * 0.95;

      else return price;
		}

	else if(0 < Date.now() - _deadline <= n){
      if(_deadline - Date.now() <= 1){
	      return price;
      }

      else{
        for(i = 0; i < (Date.now() - _deadline); i++){
            price = price * 0.95;
        }
      }

      return price;
		}

	else if( Date.now() - _deadline > n){
        for(i = 0; i < (Date.now() - _deadline); i++){
            price = price * 0.95;
        }

        return price;
		}

  }
}