package ggc.core;

public class SelectionStatus implements Status {
  @Override
  public double getAmountToPay(Partner partner, Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);
    int i;

    if (difference <= n){
      price *= 0.9;
      partner.changePoints(price*10);
			partner.setStatus();
    }

    else if (difference <= 0 && difference > n) {
      if (difference >= -2)
        price *= 0.95;
    }

    else if (difference > 0 && difference <= n) {
      if (difference > 1) {
        for (i = 0; i < difference; i++)
          price += price * 0.02;
      }
    }

    else if (difference > n) {
      for (i = 0; i < difference; i++)
        price += price * 0.05;
      if(difference > 2){
        partner.changePoints(-(partner.getPoints() * 0.1) );
      }
    }
    return price;
  }

  @Override
  public String getName() {
    return "SELECTION";
  }
}