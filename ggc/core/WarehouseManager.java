package ggc.core;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.BadEntryException;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Map;

/** Facade for access. */
public class WarehouseManager {

  /** Name of file storing current warehouse. */
  private String _filename = "";

  /** The wharehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  public Warehouse getWarehouse() {
    return _warehouse;
  }

  public Partner getPartner(String id) throws UnknownUserCoreException {
    return _warehouse.getPartner(id);
  }

  public Map<String, Partner> getPartners() {
    return _warehouse.getPartners();
  }

  public void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
    _warehouse.registerPartner(name, id, address);
  }

  public Product getProduct(String id) throws UnknownProductCoreException {
    return _warehouse.getProduct(id);
  }

  public Map<String, Product> getProducts() {
    return _warehouse.getProducts();
  }

  public void registerProduct(Product product) throws BadEntryException {
    _warehouse.registerProduct(product);
  }

  public List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
    return _warehouse.getBatchesByPartner(id);
  }

  public List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
    return _warehouse.getBatchesByProduct(id);
  }

  public List<Batch> getAllBatches() {

    return _warehouse.getAllBatches();
  }

  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {

  }

  /**
   * @@param filename
   * @@throws MissingFileAssociationException
   * @@throws IOException
   * @@throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @@param filename
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException, IOException {
    ObjectInputStream obIn = null;
    try {
      FileInputStream fpin = new FileInputStream(filename);
      ObjectInputStream objIn = new ObjectInputStream(fpin);
      obIn = new ObjectInputStream(objIn);
    Object anObject = obIn.readObject();

    } finally {
    if (obIn != null)
    obIn.close();
    }
    
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException /* FIXME maybe other exceptions */ e) {
      throw new ImportFileException(textfile, e);
    }
  }
}
