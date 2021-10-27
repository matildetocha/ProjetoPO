package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.FileOpenFailedException;
import ggc.core.WarehouseManager;
import ggc.core.exception.MissingFileAssociationException;

import java.io.FileNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);

  }

  @Override
  public final void execute() throws CommandException {
    try {
 
      if ((_receiver.getFilename()).equals(""))
        {
          addStringField("filename", Message.newSaveAs());
        _receiver.saveAs(stringField("filename"));
      }
        
      _receiver.save();
    }catch (FileNotFoundException e){
      e.printStackTrace();
    
      } catch (MissingFileAssociationException e) {
        e.printStackTrace();
      }catch (IOException e) {
        e.printStackTrace();

    }


  }

}
