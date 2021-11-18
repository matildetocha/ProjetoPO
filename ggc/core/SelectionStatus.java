package ggc.core;

public class SelectionStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);
    int i;

    if (difference >= n) //p1
      price *= 0.9;

    else if (difference >= 0 && difference < n) { // p2
      if (difference >= 2)
        price *= 0.95;
    }

    else if (-difference > 0 && -difference <= n) { //p3
      if (-difference >= 1) {
        for (i = 0; i < -difference; i++)
          price += price * 0.02;
      }
    }

    else if (-difference > n) { //p4
     //for (i = 0; i < -difference; i++)
      price += price * (0.05 * -difference);
    }
    return price;
  }

  @Override
  public void changePoints(Partner partner, Date currentDate, Date deadline, double price ,int n) {
    double points = partner.getPoints();
    int difference = currentDate.difference(deadline);

    if (difference >= 0){
      points += 10 * price;
      partner.changePoints(points);
      partner.setStatus();
    }
    else if (-difference > 2) {
      points *= 0.10;
      partner.changePoints(points);
      partner.changeStatus(new NormalStatus());
    }
  }

  @Override
  public String getName() {
    return "SELECTION";
  }
}