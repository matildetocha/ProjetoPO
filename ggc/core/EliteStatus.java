package ggc.core;

public class EliteStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);

    if (difference >= n)
      price *= 0.9;

    else if (difference >= 0 && difference < n)
      price *= 0.9;

    else if (-difference > 0 && -difference <= n)
      price *= 0.95;

    return price;
  }

  @Override
  public void changePoints(Partner partner, Date currentDate, Date deadline, double price, int n) {
    double points = partner.getPoints();
    int difference = currentDate.difference(deadline);

    if (difference >= 0){
      points += 10 * price;
      partner.changePoints(points);
      partner.setStatus();
    }

    else if (-difference > 15) {
      points *= 0.25;
      partner.changePoints(points);
      partner.changeStatus(new SelectionStatus());        
    }
  }

  @Override
  public String getName() {
    return "ELITE";
  }
}