package com.esgi.framework_JEE.slot.validation;

import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.user.validation.UserValidationService;

import java.util.Date;

public class SlotValidationService {

    private final UserValidationService userValidationService = new UserValidationService();

    public boolean isValid(Slot slot) {
        if(slot.getEndSlot() == null || slot.getStartSlot() == null || !userValidationService.isUserValid(slot.getUser()))
            return false;
        Date end;
        Date start;
        try{
            end = DateManipulator.reformatDate(slot.getEndSlot());
            start = DateManipulator.reformatDate(slot.getStartSlot());
            return !end.before(start) && !start.after(end);
        }catch (Exception e){
            return false;
        }
    }
}
