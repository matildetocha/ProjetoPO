package ggc.core;

//FIXME import classes (cannot import from pt.tecnico or ggc.app)

import ggc.core.classes.Partner;
import ggc.core.classes.transactions.Acquisition;
import ggc.core.Warehouse;
// nao posso dar import em message pq ta em app

import java.io.Serializable;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import ggc.core.exception.BadEntryException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.MissingFileAssociationException;

import pt.tecnico.uilib.Display;
/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current warehouse. */
  private String _filename = "";

  /** The wharehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  //FIXME define other attributes


  //FIXME define constructor(s)
  //FIXME define other methods

//eu coloquei a lista _partners como publica e dei import mas msm assim n ta a ler, idk why
//estas funcoes deve ser na app i dont know

public Partner getPartner(String id){
  
  Iterator<Partner> iterator = _warehouse.getPartners().iterator();
  while (iterator.hasNext()) {

    Partner partner = iterator.next();
      if (partner.getId().equals(id)) {
          return partner;
      }
  }
  return null;
}

 // public List<Partner> getPartners(){}


public void registerPartner(Partner partner) throws BadEntryException{
  for (Partner part : _warehouse.getPartners()){

    if(part.getId().equals(partner.getId())){
      throw new BadEntryException(partner.getId());
    }
  }
  _warehouse.getPartners().add(partner);
}

public void removePartner(Partner partner){

  _warehouse.getPartners().remove(partner);
}

public void displayPartners(){
  Display _display = new Display();
  for (Partner partner : _warehouse.getPartners())
    _display.addLine(partner.toString());
  _display.display();
}

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
