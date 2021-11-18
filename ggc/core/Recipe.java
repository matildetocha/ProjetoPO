package ggc.core;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
    double price = 0;

    for (Component c : _components) {
     // for (Batch batch : c.getProduct().getBatchesToSell(amountToCreate * c.getQuantity())) {
        price += (1 + _aggravation) * c.getQuantity() * c.getProduct().getMinPrice();
      //}
    } //FIXME não faço ideia como fazer isto, mas sei qula é o problema
    // ! a componente vidro (teste 19-07) é retirada de =/'s batches, logo o preço de cada parte é =/
    // ! mas não dá com esta fórmula, sendo que este é um preço unitário
    // ! mas no total, a conta a realizar (ao vender) é (1+0.1)(10*2*3 + 5(1+2+3)) --> preço da venda do agregado criado
    // ! qual é o preço unitario da batch tho? criamos 3 batches de garrafa? 1 para cada valor =/ do vidro??

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
