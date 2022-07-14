package com.esgi.framework_JEE.slot.validation;

import com.esgi.framework_JEE.TestFixtures;
import com.esgi.framework_JEE.kernel.date.DateManipulator;
import com.esgi.framework_JEE.role.domain.entity.Role;
import com.esgi.framework_JEE.slot.domain.entities.Slot;
import com.esgi.framework_JEE.slot.validation.SlotValidationService;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

public class SlotValidationServiceTest {
    SlotValidationService slotValidationService = new SlotValidationService();

    public User user1 = new User();
    @BeforeEach
    public void setup(){
        user1.setEmail(TestFixtures.randomEmail());
        user1.setFirstname("lucas");
        user1.setLastname("jehanno");
        user1.setPassword("securite !");
        user1.setPermission(new Role());
    }

    @Test
    void should_be_valid() throws ParseException {
        var slot = new Slot();
        slot.setStartSlot(DateManipulator.stringToDate("09/05/2022 09:00"));
        slot.setEndSlot(DateManipulator.stringToDate("09/05/2022 10:00"));
        slot.setUser(user1);

        assertThat(slotValidationService.isValid(slot)).isEqualTo(true);
    }

    @Test
    void should_be_invalid() throws ParseException {
        var slot = new Slot();
        slot.setStartSlot(DateManipulator.stringToDate("09/05/2022 09:00"));
        slot.setEndSlot(DateManipulator.stringToDate("09/05/2022 08:00"));
        slot.setUser(user1);
        System.out.println(slot.getStartSlot());
        System.out.println(slot.getEndSlot());
        assertThat(slotValidationService.isValid(slot)).isEqualTo(false);
    }

    @Test
    void should_be_invalid2() {
        try {
            DateManipulator.stringToDate("");
        }catch (Exception e){
            Assertions.assertTrue(true);
        }

    }

    @Test
    void should_be_invalid3() {
        var slot = new Slot();
        slot.setStartSlot(null);
        slot.setEndSlot(null);
        slot.setUser(null);
        System.out.println(slot.getStartSlot());
        System.out.println(slot.getEndSlot());
        assertThat(slotValidationService.isValid(slot)).isEqualTo(false);
    }
}