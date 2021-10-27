package ggc.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Reader;

import java.util.ArrayList;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.BadEntryException;

public class Parser {
  private Warehouse _store;

  public Parser(Warehouse w) {
    _store = w;
  }

  void parseFile(String filename) throws IOException, BadEntryException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = reader.readLine()) != null)
        parseLine(line);
    }
  }

  private void parseLine(String line) throws BadEntryException, BadEntryException {
    String[] components = line.split("\\|");

    switch (components[0]) {
    case "PARTNER":
      parsePartner(components, line);
      break;
    case "BATCH_S":
      parseSimpleProduct(components, line);
      break;

    case "BATCH_M":
      parseAggregateProduct(components, line);
      break;

    default:
      throw new BadEntryException("Invalid type element: " + components[0]);
    }
  }

  // PARTNER|id|nome|endereço
  private void parsePartner(String[] components, String line) throws BadEntryException {
    if (components.length != 4)
      throw new BadEntryException("Invalid partner with wrong number of fields (4): " + line);

    String id = components[1];
    String name = components[2];
    String address = components[3];
    try {
      _store.registerPartner(name, id, address);
    } catch (DuplicatePartnerCoreException e) {
      throw new BadEntryException("");
    }
  }

  // BATCH_S|idProduto|idParceiro|preço|stock-actual
  private void parseSimpleProduct(String[] components, String line) throws BadEntryException {
    if (components.length != 5)
      throw new BadEntryException("Invalid number of fields (4) in simple batch description: " + line);

    String idProduct = components[1];
    
    String idPartner = components[2];
    double price = Double.parseDouble(components[3]);
    int stock = Integer.parseInt(components[4]);

    try {
      if (!_store.getProducts().containsKey(idProduct)) {
        Product simpleProduct = new SimpleProduct(idProduct);
        _store.registerProduct(simpleProduct);
      }
      Product product = _store.getProduct(idProduct);
      Partner partner = _store.getPartner(idPartner);
      
      Batch batch = new Batch(product, partner, price, stock);
      
      product.addBatch(batch);
      partner.addBatch(batch);

    } catch (UnknownUserCoreException | UnknownProductCoreException e) {
      throw new BadEntryException("");
    }
  }

  // BATCH_M|idProduto|idParceiro|preço|stock-actual|agravamento|componente-1:quantidade-1#...#componente-n:quantidade-n
  private void parseAggregateProduct(String[] components, String line) throws BadEntryException {
    if (components.length != 7)
      throw new BadEntryException("Invalid number of fields (7) in aggregate batch description: " + line);

    String idProduct = components[1];
    String idPartner = components[2];
    double price = Double.parseDouble(components[3]);
    int stock = Integer.parseInt(components[4]);
    double aggravation = Double.parseDouble(components[5]);

    try {
      if (!_store.getProducts().containsKey(idProduct)) {
        ArrayList<Product> products = new ArrayList<>(); // ? wtf is this
        ArrayList<Integer> quantities = new ArrayList<>(); // ? wtf is this
        Recipe recipe = new Recipe(aggravation);

        for (String componentString : components[6].split("#")) {
          String[] recipeComponent = componentString.split(":");

          // quantities.add(Integer.parseInt(recipeComponent[1]));

          if (!_store.getProducts().containsKey(recipeComponent[0]))
            throw new UnknownProductCoreException();

          Component component = new Component(Integer.parseInt(recipeComponent[1]),
              _store.getProduct(recipeComponent[0]));
          recipe.addComponent(component);
        }
        Product aggregateProduct = new AggregateProduct(idProduct, recipe);
        _store.registerProduct(aggregateProduct);
      }
      Product product = _store.getProduct(idProduct);
      Partner partner = _store.getPartner(idPartner);
      Batch batch = new Batch(product, partner, price, stock);

      product.addBatch(batch);
      partner.addBatch(batch);
    } catch (UnknownUserCoreException | UnknownProductCoreException e) {
      throw new BadEntryException("");
    }
  }
}
