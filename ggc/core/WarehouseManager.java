package ggc.core;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.UnavailableFileException;

import java.io.Serializable;
import java.io.IOException;
import java.io.FileNotFoundException;

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

  public Partner getPartner(String id) throws BadEntryException {
    return _warehouse.getPartner(id);
  }

  public Map<String, Partner> getPartners() {
    return _warehouse.getPartners();
  }

  public void registerPartner(String name, String id, String address) throws BadEntryException {
    _warehouse.registerPartner(name, id, address);
  }

// public void registerProduct(Product product) throws BadEntryException{
//   for (Product prod : _warehouse.getProducts()){

//     if(prod.getId().equals(product.getId())){
//       throw new BadEntryException(product.getId());
//     }
//   }
//   _warehouse.getProducts().add(product);
// }


// public void registerBatch(Batch batch) throws BadEntryException{
//   _warehouse.getBatches().add(batch);
// }

  // public List<Partner> getPartnersManager(){
  //   return _warehouse.getPartners();
  // }

  // public List<Product> getProductsManager(){
  //   return _warehouse.getProducts();
  // }  



  /**
   * @@throws IOException
   * @@throws FileNotFoundException
   * @@throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    //FIXME implement serialization method
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
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException  {
    //FIXME implement serialization method
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
