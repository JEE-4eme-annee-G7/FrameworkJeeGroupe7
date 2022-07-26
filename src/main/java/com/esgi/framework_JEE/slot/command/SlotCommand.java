package com.esgi.framework_JEE.slot.command;


import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.validation.SlotValidationService;
import com.esgi.framework_JEE.slot.web.request.SlotRequest;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.slot.domain.repository.SlotRepository;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class SlotCommand {

    final
    SlotRepository slotRepository;

    private final SlotValidationService slotValidationService = new SlotValidationService();

    public SlotCommand(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }
    //DateManipulator dateManipulator;

    public Slot create(SlotRequest slotRequest, User user) throws ParseException {
        var slot = new Slot();
        slot.setStartSlot(DateManipulator.stringToDate(slotRequest.start));
        slot.setEndSlot(DateManipulator.stringToDate(slotRequest.end));
        slot.setUser(user);
        if(slotValidationService.isValid(slot))
            return slotRepository.save(slot);
        return null;
    }


    public Slot changeStart(int id,SlotRequest request) throws ParseException{
        Optional<Slot> dbSlot = Optional.ofNullable(slotRepository.findById(id));
        if(dbSlot.isPresent() && !request.start.equals("")){
            dbSlot.get().setStartSlot(DateManipulator.stringToDate(request.start));
            if(slotValidationService.isValid(dbSlot.get()))
                return slotRepository.save(dbSlot.get());
        }
        return null;
    }


    public Slot changeEnd(int id,SlotRequest request) throws ParseException{
        Optional<Slot> dbSlot = Optional.ofNullable(slotRepository.findById(id));
        if(dbSlot.isPresent() && !request.end.equals("")){
            dbSlot.get().setEndSlot(DateManipulator.stringToDate(request.end));
            if(slotValidationService.isValid(dbSlot.get()))
                return slotRepository.save(dbSlot.get());
        }
        return null;
    }

    public void delete(int id){
        Optional<Slot> dbSlot = Optional.ofNullable(slotRepository.findById(id));
        dbSlot.ifPresent(slot -> {
            slot.setUser(null);
            slotRepository.save(slot);
            slotRepository.delete(slot);
        });
    }
}
