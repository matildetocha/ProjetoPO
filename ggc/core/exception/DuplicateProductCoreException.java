package ggc.core.exception;

public class DuplicateProductCoreException extends Exception {
	private static final long serialVersionUID = 201409301048L;
	
	public DuplicateProductCoreException() {
		super("Produto Duplicado.");
	}
}