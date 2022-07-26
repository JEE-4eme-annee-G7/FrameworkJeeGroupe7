package com.esgi.framework_JEE.slot.query;

import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.slot.domain.repository.SlotRepository;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class SlotQuery {
    private final SlotRepository slotRepository;

    @Autowired
    public SlotQuery(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Slot getById(int id){
        return slotRepository.findById(id);
    }

    public List<Slot> getAll(){
        return slotRepository.findAll();
    }

    public List<Slot> findByStartSlot(String start) throws ParseException {
        return slotRepository.findAllByStartSlot(DateManipulator.stringToDate(start));
    }

    public List<Slot> findSlotsByInterval(String startInterval, String endInterval) throws ParseException {
        return slotRepository.findSlotsByStartSlotAfterAndStartSlotBefore(
                DateManipulator.stringToDate(
                        startInterval
                ),
                DateManipulator.stringToDate(
                        endInterval
                )
        );
    }

    public List<Slot> findAllByUser(User user){
        return slotRepository.findAllByUser(user);
    }
}
