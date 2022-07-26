package com.esgi.framework_JEE.slot.domain.repository;

import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface SlotRepository extends JpaRepository<Slot,Integer> {
    Slot findById(int id);

    List<Slot> findAllByStartSlot(Date start);

    List<Slot> findSlotsByStartSlotAfterAndStartSlotBefore(Date startInterval, Date endInterval);

    List<Slot> findAllByUser(User user);
}
