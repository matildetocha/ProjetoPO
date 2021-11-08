package ggc.core;

public class EliteStatus implements Status{

  public double getAmountPaid(Date deadline, double price, int n){
    int i;
    if( (_deadline - Date.now()) >= n){

		return price * 0.9;
		}
		
	else if(0 <= _deadline - Date.now() < n){

      return price * 0.9;
		}

	else if(0 < Date.now() - _deadline <= n){

      return price * 0.95;
		}

	else if( Date.now() - _deadline > n){

        return price;
		}

  }

  public String getname(){
    return "ELITE";
  }
}