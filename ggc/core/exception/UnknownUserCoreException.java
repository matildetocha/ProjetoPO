package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;


public class UnknownUserCoreException extends CommandException {


  public UnknownUserCoreException(String key) {
    super("User Desconhecido.");
  }

}