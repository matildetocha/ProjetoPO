package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
//FIXME import classes
import ggc.core.classes.Partner;


/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    //FIXME add command fields
    addStringField("Id", "Qual o id do Parceiro?: ");
    addStringField("Nome", "Qual o nome do Parceiro?: ");
    addStringField("Morada", "Qual a morada do Parceiro?: ");
  }

  @Override
  public void execute() throws CommandException {
    //FIXME implement command
    Partner partner = new Partner(stringField("Nome"),stringField("Id"),stringField("Morada"));
    _receiver.registerPartner(partner);
  }

}
