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
  public String getName() {
    return "ELITE";
  }
}