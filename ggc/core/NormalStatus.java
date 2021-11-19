package ggc.core;

public class NormalStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);
    if (difference >= n) 
      price *= 0.9;

    else if (-difference > 0 && -difference <= n) 
      price += ((price * 0.05) * -difference);

    else if (-difference > n) 
      price += ((price * 0.1) * -difference);

    return price;
  }

  @Override
  public void changePoints(Partner partner, Date currentDate, Date deadline, double price, int n) {
    double points = partner.getPoints();
    int difference = currentDate.difference(deadline);
    if (difference >= 0) {
      points += 10 * price;
      partner.changePoints(points);
      partner.setStatus();

    } else {
      points = 0;
      partner.changePoints(points);
    }
  }

  @Override
  public String getName() {
    return "NORMAL";
  }
}