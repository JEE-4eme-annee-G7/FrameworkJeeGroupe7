package com.esgi.framework_JEE.role.application.query;

import com.esgi.framework_JEE.role.domain.entity.Role;
import com.esgi.framework_JEE.role.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleQuery {
    final
    RoleRepository roleRepository;

    public RoleQuery(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    public Role getById(int id){
        return roleRepository.findById(id);
    }

    public Role getByName(String roleName) {
        return roleRepository.findRoleByTitlePermission(roleName);
    }
}
