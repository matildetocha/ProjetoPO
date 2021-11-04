package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownTransactionKeyException;
import ggc.core.WarehouseManager;
//FIXME import classes
import ggc.core.exception.UnknownTransactionCoreException;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    //FIXME add command fields
    addIntegerField("TransactionId", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try{
    _receiver.getTransaction(integerField("TransactionId")).pay();
    }catch(UnknownTransactionCoreException e){
      throw new UnknownTransactionKeyException(integerField("TransactionId"));
    }
    
  }

}
