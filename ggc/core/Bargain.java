package ggc.core;

public class Bargain extends Notification {
  Bargain(DeliveryMethod type, Product product, double price) {
    super(type, product, price);
  }

  @Override
  public String toString() {
    return "BARGAIN" + "|" + getProduct().getId() + "|" + Math.round(getProduct().getMinPrice());
  }
}
