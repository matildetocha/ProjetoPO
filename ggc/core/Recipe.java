package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
  private static final long serialVersionUID = 400109132706L;

  private double _aggravation;
  private Product _aggregateProduct;
  private List<Component> _components;

  Recipe(double aggravation) {
    _aggravation = aggravation;
    _components = new ArrayList<>();
  }

  void addAggregateProduct(Product aggregateProduct) {
    _aggregateProduct = aggregateProduct;
  }

  void addComponent(Component component) {
    _components.add(component);
  }

  double getAlpha() {
    return _aggravation;
  }

  @Override
  public String toString() {
    String string = "";
    for (Component component : _components) {
      string += component;
    }
    return string;
  }
}
