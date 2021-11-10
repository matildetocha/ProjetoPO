package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
   super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
   //FIXME maybe add command fields
   addStringField("partnerId", Message.requestPartnerKey());
   addStringField("productId", Message.requestProductKey());
   addIntegerField("quantity", Message.requestAmount());
 }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
  }

}
