package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.app.exception.InvalidDateException;
import ggc.core.WarehouseManager;
import ggc.core.exception.InvalidDateCoreException;

/**
 * Advance current date.ewe
 */
class DoAdvanceDate extends Command<WarehouseManager> {

	DoAdvanceDate(WarehouseManager receiver) {
		super(Label.ADVANCE_DATE, receiver);
		addIntegerField("timeAdd", Message.requestDaysToAdvance());
	}

	@Override
	public final void execute() throws CommandException, InvalidDateException {
		try {
			_receiver.advanceDate(integerField("timeAdd"));
		} catch (InvalidDateCoreException e) {
			throw new InvalidDateException(integerField("timeAdd"));
		}
	}
}
