package ggc.core.exception;

/**
 * 
 */
public class UnavailableProductCoreException extends Exception {

  String _productId;
  int _requested;
  int _available;

  public UnavailableProductCoreException() {
    super("Produto Indisponível");
  }

  public UnavailableProductCoreException(String productId, int available, int requested) {
    super("Produto Indisponível");
    _productId = productId;
    _requested = requested;
    _available = available;
  }

  public String getProductId(){
    return _productId;
  }
  public int getRequested(){
    return _requested;
  }
  public int getAvailable(){
    return _available;
  }
}
