package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Date;

/**
 * Show current date.
 */
class DoDisplayDate extends Command<WarehouseManager> {

  DoDisplayDate(WarehouseManager receiver) {
    super(Label.SHOW_DATE, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    //FIXME implement command

    _display.popup(Date.now());

  }

}
