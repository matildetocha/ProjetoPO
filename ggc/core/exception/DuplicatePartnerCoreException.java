package ggc.core.exception;

public class DuplicatePartnerCoreException extends Exception {
	private static final long serialVersionUID = 201409301048L;
	
	public DuplicatePartnerCoreException() {
		super("Parceiro Duplicado.");
	}
}