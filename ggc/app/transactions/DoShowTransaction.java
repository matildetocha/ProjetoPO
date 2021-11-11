package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnknownTransactionKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownTransactionCoreException;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("transactionId", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _display.popup(_receiver.getTransaction(integerField("transactionId")));
    } catch (UnknownTransactionCoreException e) {
      throw new UnknownTransactionKeyException(integerField("transactionId"));
    }
  }
}
