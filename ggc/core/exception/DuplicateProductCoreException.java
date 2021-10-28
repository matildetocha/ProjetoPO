package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;

public class DuplicateProductCoreException extends CommandException {
	public DuplicateProductCoreException() {
		super("Produto Duplicado.");
	}
}