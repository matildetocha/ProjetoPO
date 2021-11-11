package ggc.core;

import java.io.Serializable;

public class DefaultMethod implements DeliveryMethod, Serializable {
  private static final long serialVersionUID = 202109192006L;
  
  @Override
  public void sendNewNotification(Product product) {
    Notification notification = new New(this, product, product.getMaxPrice());
    product.notifyAllObservers(notification);
  }

  @Override
  public void sendBargainNotification(Product product) {
    Notification notification = new Bargain(this, product, product.getMinPrice());
    product.notifyAllObservers(notification);
  }
}
