package ggc.app.main;

import ggc.app.exception.FileOpenFailedException;
import ggc.core.WarehouseManager;
import ggc.core.exception.MissingFileAssociationException;

import java.io.IOException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

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
      if (("").equals(_receiver.getFilename())) {
        Form form = new Form("File");
        form.addStringField("filename", Message.newSaveAs());
        form.parse();
        _receiver.saveAs(form.stringField("filename"));
      }
      else
        _receiver.save();
    }
    catch (MissingFileAssociationException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      throw new FileOpenFailedException(_receiver.getFilename());
    }
  }
}
