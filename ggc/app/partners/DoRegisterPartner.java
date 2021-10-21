package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.DuplicatePartnerKeyException;
import ggc.core.WarehouseManager;
//FIXME import classes
import ggc.core.classes.Partner;
import ggc.core.exception.BadEntryException;


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
  public void execute() throws CommandException, DuplicatePartnerKeyException {
    //FIXME implement command
    //nao sei se criar o parceiro assim é muito bom
    try{


    Partner partner = new Partner(stringField("Nome"),stringField("Id"),stringField("Morada"));
    _receiver.registerPartner(partner);
    }catch(BadEntryException e){

      throw new DuplicatePartnerKeyException(stringField("Id"));
    }
  }

}
