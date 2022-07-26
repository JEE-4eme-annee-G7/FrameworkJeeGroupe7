package com.esgi.framework_JEE.role.domain.repository;

import com.esgi.framework_JEE.role.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int id);

    Role findRoleByTitlePermission(String name);
}
