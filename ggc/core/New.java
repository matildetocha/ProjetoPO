package ggc.core;

public class New extends Notification {
  New(Notification type, Product product, double price) {
    super(type, product, price);
  }
  
  @Override
  public String toString() {
    return "NEW";
  }
  
}
