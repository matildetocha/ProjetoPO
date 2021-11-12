package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe implements Serializable {
  private static final long serialVersionUID = 202109192006L;

  private double _aggravation;
  private List<Component> _components;

  Recipe(double aggravation) {
    _aggravation = aggravation;
    _components = new ArrayList<>();
  }

  double getAlpha() {
    return _aggravation;
  }

  void addComponent(Component component) {
    _components.add(component);
  }

  List<Component> getComponents() {
    return Collections.unmodifiableList(_components);
  }

  double getPrice(int amountToCreate) {
    List<Batch> batchesToSell;
    double price = 0;
    
    for(Component c : getComponents()){
      batchesToSell = c.getProduct().getBatchesToSell(amountToCreate);
      price += c.getProduct().getPriceByFractions(batchesToSell, amountToCreate);
    }

    return price;
  }

  @Override
  public String toString() {
    String string = "";
    for (Component component : _components) {
      string += component + "#";
    }

    string = string.substring(0, string.length() - 1);
    return string;
  }
}
