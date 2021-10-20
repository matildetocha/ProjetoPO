package ggc.core;


// FIXME import classes (cannot import from pt.tecnico or ggc.app)

import java.io.Serializable;
import java.io.IOException;
import ggc.core.exception.BadEntryException;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import ggc.core.classes.Partner;
import ggc.core.classes.transactions.Acquisition;
import ggc.core.classes.Date;

/**TESTE TESTE no windows
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  // FIXME define attributes
  
  private Date _date;
  private int _nextTransactionId;
  public List<Partner> _partners;

  // FIXME define contructor(s)
  public Warehouse() {
    _partners = new ArrayList<>();
  }
  // FIXME define methods



  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException /* FIXME maybe other exceptions */ {
    //FIXME implement method
  }

}
