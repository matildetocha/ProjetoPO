package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;

public class InvalidDateCoreException extends CommandException {

	public InvalidDateCoreException() {
		super("Data inválida!");
	}
}