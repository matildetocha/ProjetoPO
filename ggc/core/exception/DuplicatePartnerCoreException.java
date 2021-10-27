package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;

public class DuplicatePartnerCoreException extends CommandException {
  public DuplicatePartnerCoreException() {
    super("Parceiro Duplicado.");
  }
}