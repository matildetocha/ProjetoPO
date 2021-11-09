package ggc.core;

public interface Status {
  double getAmountToPay(Date currentDate, Date deadline, double price, int n);
  String getName();
}