package ggc.core.exception;

import pt.tecnico.uilib.menus.CommandException;

public class UnknownTransactionCoreException extends CommandException {

	public UnknownTransactionCoreException() {
		super("Transação Desconhecida.");
	}
}