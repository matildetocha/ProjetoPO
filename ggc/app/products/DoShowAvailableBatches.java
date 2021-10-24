package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.ArrayList;
import java.util.List;
import ggc.core.Batch;
import ggc.core.Partner;
import java.util.Iterator;
import java.util.Set;

import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Show available batches.
 */
class DoShowAvailableBatches extends Command<WarehouseManager> {

  DoShowAvailableBatches(WarehouseManager receiver) {
    super(Label.SHOW_AVAILABLE_BATCHES, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command


    //for(Partner part : _receiver.getPartners())
      //orderedBatches.add(_receiver.getBatchesByPartner(part.getId()));




  }
}

