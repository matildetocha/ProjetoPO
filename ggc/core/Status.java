package ggc.core;

public interface Status {
  double getAmountToPay(Partner partner, Date currentDate, Date deadline, double price, int n);
  String getName();
}