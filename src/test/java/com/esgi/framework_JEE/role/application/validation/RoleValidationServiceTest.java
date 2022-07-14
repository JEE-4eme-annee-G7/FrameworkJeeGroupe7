package com.esgi.framework_JEE.role.application.validation;

import com.esgi.framework_JEE.role.domain.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class RoleValidationServiceTest {
    private final RoleValidationService roleValidationService = new RoleValidationService();

    @Test
    void isValid() {
        assertThat(roleValidationService.isValid(null)).isFalse();
        var role = new Role();
        role.setTitlePermission("");
        assertThat(roleValidationService.isValid(role)).isFalse();
        role.setTitlePermission("role");
        assertThat(roleValidationService.isValid(role)).isTrue();
    }
}