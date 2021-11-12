package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.core.WarehouseManager;
import ggc.app.exception.UnavailableProductException;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownProductCoreException;

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
      _receiver.registerSaleByCredit(stringField("productId"), stringField("partnerId"), integerField("deadline"),
          integerField("quantity"));
    } catch (UnavailableProductCoreException e) {
      if (!_receiver.isAggregateProduct(stringField("productId")))
        throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
            _receiver.getAvailableStock(stringField("productId")));
            
      try {
        _receiver.saleAggProduct(stringField("partnerId"), stringField("productId"), integerField("deadline"),
            integerField("quantity"));
      } catch (DuplicateProductCoreException | UnknownProductCoreException e1) {
        throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
            _receiver.getAvailableStock(stringField("productId")));
      }catch(UnavailableProductCoreException e2){
        throw new UnavailableProductException(e2.getProductId(), e2.getRequested(), e2.getAvailable());
      }
    }
  }

}
