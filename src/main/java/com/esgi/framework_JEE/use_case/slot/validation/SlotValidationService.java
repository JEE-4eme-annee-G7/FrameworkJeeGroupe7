package com.esgi.framework_JEE.use_case.slot.validation;

import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.use_case.slot.domain.entities.Slot;

import java.text.ParseException;
import java.util.Date;

public class SlotValidationService {

    public boolean isValid(Slot slot) throws ParseException {
        if(slot.getEndSlot() == null || slot.getStartSlot() == null)
            return false;
        var end = DateManipulator.stringToDate(slot.getEndSlot());
        var start = DateManipulator.stringToDate(slot.getStartSlot());



        if(end.before(start))
            return false;

        return !start.after(end);
        //todo fonctionne pas comprends pas trop trop pourquoi
    }
}
