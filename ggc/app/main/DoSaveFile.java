package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

//FIXME import classes

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
    // FIXME implement command and create a local Form
    // public class IOTest {
    //   public static void main(String[] args) {
    //     try (BufferedReader myInput = new BufferedReader(new FileReader(args[0]));
    //         BufferedWriter myOutput = new BufferedWriter(new FileWriter(args[1]));) {
    //       int c;
    //       while ((c = myInput.read()) != -1) {
    //         myOutput.write(c);
    //       }
    //     } catch (IOException e) {
    //       System.out.println("Error while copying " + e.getMessage());
    //       e.printStackTrace();
    //     }
    //   }
    // }
  }

}
