package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.classes.Partner;

import java.util.List;
import java.util.ArrayList;
//FIXME import classes

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

  DoShowAllPartners(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PARTNERS, receiver);

  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    _receiver.displayPartners();
  }

}
