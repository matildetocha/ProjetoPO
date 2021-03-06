package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnknownTransactionKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.AlreadyPaidTransactionCoreException;
import ggc.core.exception.UnknownTransactionCoreException;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("TransactionId", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.payTransaction(integerField("TransactionId")); 
    } catch (UnknownTransactionCoreException e) {
      throw new UnknownTransactionKeyException(integerField("TransactionId"));
    } catch (AlreadyPaidTransactionCoreException e) {
      e.getStackTrace();
    }

  }

}
