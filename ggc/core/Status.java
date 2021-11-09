package ggc.core;

public interface Status {
  double getAmountToPay(Date currentDate, Date deadline, double price, int n);
  double getPoints(Date currentDate, Date deadline, int n);
  String getName();
}