package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;


public class UnknownProductCoreException extends CommandException {


  public UnknownProductCoreException(String key) {
    super("Produto Desconhecido.");
  }

}