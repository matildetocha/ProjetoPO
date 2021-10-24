package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;

import java.util.Iterator;
import java.util.Set;

import ggc.core.WarehouseManager;

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

  DoShowAllPartners(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PARTNERS, receiver);
  }

  @Override
  public void execute() {
    Set<String> keys = _receiver.getPartners().keySet();
		Iterator<String> iterator = keys.iterator();

		while (iterator.hasNext()) 
      _display.popup(_receiver.getPartners().get(iterator.next()));
  }
}
