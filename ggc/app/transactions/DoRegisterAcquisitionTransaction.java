package ggc.app.transactions;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.core.WarehouseManager;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.UnknownProductCoreException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);

    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      try {
        
        _receiver.registerAcquisition(stringField("partnerId"), stringField("productId"), realField("price"),
            integerField("quantity"));
        _receiver.changeGlobalBalance(-(realField("price") * integerField("quantity")));
            
      } catch (UnknownProductCoreException e) {
        addStringField("answer", Message.requestAddRecipe());

        if (stringField("answer").equals("y")) {
          addIntegerField("numberComponents", Message.requestNumberOfComponents());
          addRealField("alpha", Message.requestAlpha());

          List<String> productIds = new ArrayList<>();
          List<Integer> quantities = new ArrayList<>();

          for (int i = 0; i < integerField("numberComponents"); i++) {
            addIntegerField("quantity", Message.requestAmount());
            addStringField("productId", Message.requestProductKey());

            productIds.add(stringField("productId"));
            quantities.add(integerField("quantity"));
          }

          _receiver.createAggregateProduct(stringField("productId"), realField("alpha"), productIds, quantities,
              integerField("numberComponents"));
          _receiver.registerAcquisition(stringField("partnerId"), stringField("productId"), realField("price"),
              integerField("quantity"));
          _receiver.changeGlobalBalance(-(realField("price") * integerField("quantity")));

        } else{       
          _receiver.createSimpleProduct(stringField("productId"));
          _receiver.changeGlobalBalance(-(realField("price") * integerField("quantity")));
        }
      }
    } catch (DuplicateProductCoreException | UnknownProductCoreException e) {
      e.printStackTrace();
    }
  }
}