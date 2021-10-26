package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.UnknownPartnerKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.BadEntryException;
import ggc.core.exception.UnknownProductCoreException;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("id", Message.requestProductKey());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _display.popup(_receiver.getBatchesByProduct(stringField("id")));
    } catch (UnknownProductCoreException e) {
      throw new UnknownPartnerKeyException(stringField("id"));
    }
  }
}