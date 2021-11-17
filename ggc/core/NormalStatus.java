package ggc.core;

public class NormalStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);

    if (difference >= n)
      price *= 0.9;

    else if (-difference > 0 && -difference <= n) {
      for (int i = 0; i < -difference; i++)
        price += price * 0.05;
    }

    else if (-difference > n) {
      for (int i = 0; i < -difference; i++)
        price += price * 0.1;
    }

    return price;
  }

  @Override
  public double getPoints(Partner partner, Date currentDate, Date deadline, double price, int n) {
    double points = 0;
    int difference = currentDate.difference(deadline);

    if (difference >= 0)
      points += 10 * price;

    return points;
  }

  @Override
  public String getName() {
    return "NORMAL";
  }
}