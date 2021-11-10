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

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);

    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      try {
        _receiver.getProduct(stringField("productId"));
        _receiver.getPartner(stringField("partnerId"));
      } catch (UnknownProductCoreException e) {
        throw new UnknownProductKeyException(stringField("productId"));
      } catch (UnknownUserCoreException e) {
        throw new UnknownPartnerKeyException(stringField("partnerId"));
      }
      if (_receiver.isAggregateProduct(stringField("productId")))
        _receiver.registerBreakdown(stringField("partnerId"), stringField("productId"), integerField("quantity"));
    } catch (UnavailableProductCoreException e) {
      throw new UnavailableProductException(stringField("productId"), integerField("quantity"),
          _receiver.getAvailableStock(stringField("productId")));
    }
  }
}
