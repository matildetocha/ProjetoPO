package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;

/**
 * Show global balance.
 */
class DoShowAccountingBalance extends Command<WarehouseManager> {

  DoShowAccountingBalance(WarehouseManager receiver) {
    super(Label.SHOW_BALANCE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    _display.popup(Message.currentBalance(_receiver.getAvailableBalance(), _receiver.getAccountingBalance()));
  }
  
}
