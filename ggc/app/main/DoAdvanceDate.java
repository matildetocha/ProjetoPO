package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.classes.Date;
//FIXME import classes

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("timeAdd", "Quantos dias quer avançar no tempo?: ");
    //FIXME add command fields
  }

  @Override
  public final void execute() throws CommandException {

    Date.add(integerField("timeAdd"));
    _display.popup("" + Date.now());
  }

}
