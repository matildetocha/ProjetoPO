package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
//FIXME import classes
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownProductCoreException;

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
    try{
      if(_receiver.isAggregateProduct(stringField("productId"))){
        _receiver.registerBreakdown(stringField("partnerId"), stringField("productId"), integerField("quantity"));

      }
    }catch(UnavailableProductCoreException e){
      throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
      _receiver.getAvailableStock(stringField("productId")));
    }catch(UnknownProductCoreException e){
      throw new UnknownProductKeyException(stringField("productId"));
    }
  }

}
