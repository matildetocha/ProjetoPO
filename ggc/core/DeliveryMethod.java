package ggc.core;

public interface DeliveryMethod {
  void sendNewNotification(Product product);
  void sendBargainNotification(Product product);
}
