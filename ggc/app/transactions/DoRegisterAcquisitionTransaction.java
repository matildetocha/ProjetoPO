package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    //FIXME maybe add command fields
    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    _receiver.registerAcquisiton(stringField("partnerId"), stringField("productId"), realField("price"), integerField("quantity"));
  }

}
