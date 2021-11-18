package ggc.core.exception;

public class UnknownTransactionCoreException extends Exception {
	private static final long serialVersionUID = 201409301048L;
	
	public UnknownTransactionCoreException() {
		super("Transação Desconhecida.");
	}
}