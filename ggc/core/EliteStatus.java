package ggc.core;

public class EliteStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);

    if (difference <= n)
      price *= 0.9;

    else if (difference <= 0 && difference > n)
      price *= 0.9;

    else if (difference > 0 && difference <= n)
      price *= 0.95;

    return price;
  }

  @Override
  public double getPoints(Partner partner, Date currentDate, Date deadline, double price, int n) {
    double points = 0;
    int difference = currentDate.difference(deadline);

    if (difference <= n)
      points += 10 * price;

    else if (-difference >= 15) {
      points *= 0.25;
      partner.changeStatus(new SelectionStatus());
    }
    return points;
  }

  @Override
  public String getName() {
    return "ELITE";
  }
}