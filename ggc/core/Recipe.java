package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Recipe implements Serializable {
  private static final long serialVersionUID = 202109192006L;

  private double _aggravation;
  private Product _aggregateProduct;
  private List<Component> _components;

  Recipe(double aggravation) {
    _aggravation = aggravation;
    _components = new ArrayList<>();
  }

  double getAlpha() {
    return _aggravation;
  }

  void addAggregateProduct(Product aggregateProduct) {
    _aggregateProduct = aggregateProduct;
  }

  void addComponent(Component component) {
    _components.add(component);
  }

  List<Component> getComponents() {
    return Collections.unmodifiableList(_components);
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
