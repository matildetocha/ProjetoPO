package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnavailableProductException;
import ggc.core.WarehouseManager;
//FIXME import classes

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    //FIXME maybe add command fields 
    addStringField("partnerId", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("productId", Message.requestProductKey());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    //FIXME implement command
    try{
      //_receiver.aggregateProducts(productIds, quantitys, stringField("partnerId"), integerField("numberComponents"));
      _receiver.registerSale(integerField("quantity"), stringField("productId"), stringField("partnerId"), integerField("deadline"));
    }catch(UnavailableProductException e){
      throw new UnavailableProductException(stringField("productId"), integerField("quantity"), _receiver.getAvailableStock(stringField("productId")));
    }
  }

}
