package ggc.app.transactions;

import java.util.ArrayList;
import java.util.List;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.WarehouseManager;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownUserCoreException;

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
      } catch (UnknownUserCoreException e) {
        throw new UnknownPartnerKeyException(stringField("partnerId"));

      } catch (UnknownProductCoreException e1) {
        if (Form.confirm(Message.requestAddRecipe())) {
          int numOfComponents = Form.requestInteger(Message.requestNumberOfComponents());
          double alpha = Form.requestReal(Message.requestAlpha());

          List<String> productIds = new ArrayList<>();
          List<Integer> quantities = new ArrayList<>();

          for (int i = 0; i < numOfComponents; i++) {
            String productId = Form.requestString(Message.requestProductKey());
            int amount = Form.requestInteger(Message.requestAmount());

            try {
              _receiver.getProduct(productId);
            } catch (UnknownProductCoreException e2) {
              throw new UnknownProductKeyException(productId);
            }

            productIds.add(productId);
            quantities.add(amount);
          }

          _receiver.createAggregateProduct(stringField("productId"), alpha, productIds, quantities, numOfComponents);
        }

        else
          _receiver.createSimpleProduct(stringField("productId"));

        try {
          _receiver.registerAcquisition(stringField("partnerId"), stringField("productId"), realField("price"),
              integerField("quantity"));
        } catch (UnknownUserCoreException e) {
          throw new UnknownPartnerKeyException(stringField("partnerId"));
        }
      }
    } catch (DuplicateProductCoreException | UnknownProductCoreException e) {
      e.printStackTrace();
    }
  }
}