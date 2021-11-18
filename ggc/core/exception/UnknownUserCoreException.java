package ggc.core.exception;

public class UnknownUserCoreException extends Exception {
	private static final long serialVersionUID = 201409301048L;
	
	public UnknownUserCoreException() {
		super("Partner Desconhecido.");
	}
}