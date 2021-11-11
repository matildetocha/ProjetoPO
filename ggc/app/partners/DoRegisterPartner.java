package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.DuplicatePartnerKeyException;

import ggc.core.WarehouseManager;
import ggc.core.exception.DuplicatePartnerCoreException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);

    addStringField("id", Message.requestPartnerKey());
    addStringField("name", Message.requestPartnerName());
    addStringField("address", Message.requestPartnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    try {
      _receiver.registerPartner(stringField("name"), stringField("id"), stringField("address"));
    } catch (DuplicatePartnerCoreException e) {
      throw new DuplicatePartnerKeyException(stringField("id"));
    }
  }
}