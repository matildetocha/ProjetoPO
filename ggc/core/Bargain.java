package ggc.core;

public class Bargain extends Notification { 
  Bargain(Notification type, Product product, double price) {
    super(type, product, price);
  }

  @Override
  public String toString() {
    return "BARGAIN";
  }
}
