package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownUserCoreException;

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
    } catch (UnknownUserCoreException e) {
      throw new UnknownProductKeyException(stringField("partnerId"));

    } catch (UnknownProductCoreException e) {
      throw new UnknownProductKeyException(stringField("productId"));

    } catch (UnavailableProductCoreException e) {
      if (!_receiver.isAggregateProduct(stringField("productId")))
        throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
            _receiver.getAvailableStock(stringField("productId")));

      try {
        _receiver.saleAggProduct(stringField("partnerId"), stringField("productId"), integerField("deadline"),
            integerField("quantity"));
      } catch(UnknownUserCoreException e1){
        throw new UnknownPartnerKeyException(stringField("partnerId"));
      } catch (UnknownProductCoreException e2) {
        throw new UnknownProductKeyException(stringField("productId"));
      } catch (UnavailableProductCoreException e3) {
        throw new UnavailableProductException(e3.getProductId(), e3.getRequested(), e3.getAvailable());
      }
    }
  }

}
