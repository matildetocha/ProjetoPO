package ggc.core.exception;

public class UnknownProductCoreException extends Exception {
	private static final long serialVersionUID = 201409301048L;
	
	public UnknownProductCoreException() {
		super("Produto Desconhecido.");
	}
}