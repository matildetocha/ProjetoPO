package ggc.app.transactions;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.UnknownTypeException;

import ggc.app.exception.UnknownProductKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.Product;
import ggc.core.Recipe;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnknownProductCoreException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    // FIXME maybe add command fields
    addStringField("partnerId", Message.requestPartnerKey());
    addStringField("productId", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("quantity", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    // FIXME implement command
    try {

      _receiver.registerAcquisiton(stringField("partnerId"), stringField("productId"), realField("price"),
          integerField("quantity"));

    } catch (UnknownProductCoreException e) {

      addIntegerField("answer", Message.requestAddRecipe());

      if (integerField("answer") == 1) {

        addIntegerField("numberComponents", Message.requestNumberOfComponents());
        addRealField("alpha", Message.requestAlpha());

        List<String> productIds = new ArrayList<>();
        List<Integer> quantitys = new ArrayList<>();

        for (int i = 0; i < integerField("numberComponents"); i++) {

          addIntegerField("quantity", Message.requestAmount());

          addStringField("productId", Message.requestProductKey());

          productIds.add(stringField("productId"));
          quantitys.add(integerField("quantity"));

        }
        //subtrai da global balance o preco do agregado criado, mas nao usa o preco pedido? idk
        _receiver.registerAggProductId(stringField("productId"),realField("alpha"), productIds, quantitys, integerField("numberComponents"));

        _receiver.registerAcquisiton(stringField("partnerId"), stringField("productId"), realField("price"), integerField("quantity"));

      } else {
        
        _receiver.registerSimpleProductId(stringField("productId"));
        _receiver.changeGlobalBalance(-( realField("price") * integerField("quantity") ));

      }
    }
  }

}
