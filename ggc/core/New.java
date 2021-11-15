package ggc.core;

public class New extends Notification {
  New(DeliveryMethod type, Product product, double price) {
    super(type, product, price);
  }

  @Override
  public String toString() {
    return "NEW" + "|" + getProduct().getId() + "|" + Math.round(getPrice());
  }
}
