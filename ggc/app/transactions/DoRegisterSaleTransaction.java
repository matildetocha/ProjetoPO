package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.core.WarehouseManager;
import ggc.app.exception.UnavailableProductException;
import ggc.core.exception.UnavailableProductCoreException;

/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);

    addStringField("partnerId", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("productId", Message.requestProductKey());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      // _receiver.aggregateProducts(productIds, quantitys, stringField("partnerId"),
      // integerField("numberComponents"));
      _receiver.registerSale(stringField("productId"), stringField("partnerId"), integerField("deadline"),
          integerField("quantity"));
    } catch (UnavailableProductCoreException e) {
      if (!_receiver.isAggregateProduct(stringField("prductId")))
        throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
            _receiver.getAvailableStock(stringField("productId")));
      
    }
  }

}
