package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownUserCoreException;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);

    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      _receiver.toogleProductNotifications(stringField("partnerId"), stringField("productId"));
    } catch (UnknownUserCoreException e) {
      throw new UnknownPartnerKeyException(stringField("partnerId"));
    } catch (UnknownProductCoreException e) {
      throw new UnknownProductKeyException(stringField("productId"));
    }
  }
}
