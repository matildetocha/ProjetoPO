package ggc.core;

public class SelectionStatus implements Status {
  @Override
  public double getAmountToPay(Date currentDate, Date deadline, double price, int n) {
    int difference = currentDate.difference(deadline);
    int i;

    if (difference <= n)
      price *= 0.9;

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
    }
    return price;
  }

  @Override
  public double getPoints(Date currentDate, Date deadline, int n) {
    double points = 0;
    int difference = currentDate.difference(deadline);

    if (difference <= n)
      points += 10;

    else if (difference >= 2)
      points *= 0.10;
    return points;
  }

  @Override
  public String getName() {
    return "SELECTION";
  }
}