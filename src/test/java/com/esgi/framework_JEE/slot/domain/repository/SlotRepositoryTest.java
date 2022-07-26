package com.esgi.framework_JEE.slot.domain.repository;

import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.slot.domain.repository.SlotRepository;
import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.Domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class SlotRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    public void should_find_empty() {
        var pc = slotRepository.findAll();
        assertThat(pc).isEmpty();
    }


    @Test
    public void should_find_by_start() throws ParseException {
        var pc = slotRepository.findAllByStartSlot(DateManipulator.stringToDate("09/05/2012 11:00"));
        assertThat(pc).isEmpty();
    }

    @Test
    public void should_find_by_start2() throws ParseException {
        var slot = new Slot();
        var user1 = new User();
        user1.setEmail("ljehanno@myges.fr");
        user1.setFirstname("lucas");
        user1.setLastname("jehanno");
        user1.setPassword("ceci est un  mot de passe tres securise !(c fo met pas ca)");
        var savedUser = userRepository.save(user1);
        slot.setStartSlot(DateManipulator.stringToDate("09/05/2012 11:00"));
        slot.setEndSlot(DateManipulator.stringToDate("09/05/2013 11:00"));
        slot.setUser(savedUser);
        slotRepository.save(slot);
        var response = slotRepository.findAllByStartSlot(DateManipulator.stringToDate("09/05/2012 11:00"));
        assertThat(response).contains(slot);
    }
}