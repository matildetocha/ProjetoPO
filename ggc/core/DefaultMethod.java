package ggc.core;

import java.io.Serializable;

public class DefaultMethod implements DeliveryMethod, Serializable {
  private static final long serialVersionUID = 202109192006L;

  @Override
  public void sendNewNotification(Product product, double price) {
    Notification notification = new New(this, product, price);
    product.notifyAllObservers(notification);
  }

  @Override
  public void sendBargainNotification(Product product, double price) {
    Notification notification = new Bargain(this, product, price);
    product.notifyAllObservers(notification);
  }
}
