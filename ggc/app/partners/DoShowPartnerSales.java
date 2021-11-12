package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnknownPartnerKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownUserCoreException;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("partnerId", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try {
      _display.popup(_receiver.getPartnerSales(stringField("partnerId")));
    } catch (UnknownUserCoreException e) {
      throw new UnknownPartnerKeyException(stringField("partnerId"));
    }
  }

}
