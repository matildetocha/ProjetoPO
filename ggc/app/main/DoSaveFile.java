package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.FileOpenFailedException;
import ggc.core.WarehouseManager;
import ggc.core.exception.MissingFileAssociationException;

import java.io.FileNotFoundException;

import java.io.BufferedWriter;
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
    if (_receiver.getFilename() == "")
      addStringField("filename", Message.newSaveAs());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.save();

    } catch (FileNotFoundException e) {
      try {
        _receiver.saveAs(stringField("filename"));
      } catch (MissingFileAssociationException | IOException ex) {
        
      }
      
    } catch (MissingFileAssociationException | IOException ex) {
      throw new FileOpenFailedException(stringField("filename"));
    }
  }
}
