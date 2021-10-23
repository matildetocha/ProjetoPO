package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.InvalidDateException;
import ggc.core.WarehouseManager;
import ggc.core.Date;
import ggc.core.exception.BadEntryException;


/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("timeAdd", Message.requestDaysToAdvance());
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException, InvalidDateException {

    try{

    Date.add(integerField("timeAdd"));

    }catch (BadEntryException e){

      throw new InvalidDateException(integerField("timeAdd"));
    }
    
  }

}
