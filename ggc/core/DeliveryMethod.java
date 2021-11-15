package ggc.core;

public interface DeliveryMethod {
  void sendNewNotification(Product product, double price);
  void sendBargainNotification(Product product, double price);
}
