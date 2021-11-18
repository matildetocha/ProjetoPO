package ggc.core.exception;

public class AlreadyPaidTransactionCoreException extends Exception {
  private static final long serialVersionUID = 201409301048L;

  public AlreadyPaidTransactionCoreException() {
    super("Transação já paga.");
  }
  
}
