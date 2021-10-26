package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnavailableFileException;
import ggc.app.exception.FileOpenFailedException;

import java.io.IOException;
import java.lang.ClassNotFoundException;

//FIXME import classes

/**
 * Open existing saved state.
 */
class DoOpenFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoOpenFile(WarehouseManager receiver) {
    super(Label.OPEN, receiver);
    //FIXME maybe add command fields
    addStringField("filename", Message.openFile());
  }

  @Override
  public final void execute() throws CommandException, FileOpenFailedException {
    
  try {  
        _receiver.load(stringField("filename"));
  //     //FIXME implement command      

  } catch (UnavailableFileException | IOException ufe) {
         throw new FileOpenFailedException(stringField("filename"));
  } catch (ClassNotFoundException e) {
         e.printStackTrace();  
  }
  }
    
  

}
