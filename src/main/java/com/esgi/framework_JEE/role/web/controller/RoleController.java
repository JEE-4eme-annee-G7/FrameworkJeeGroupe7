package com.esgi.framework_JEE.role.web.controller;


import com.esgi.framework_JEE.role.application.command.RoleCommand;
import com.esgi.framework_JEE.role.application.query.RoleQuery;
import com.esgi.framework_JEE.role.domain.entity.Role;
import com.esgi.framework_JEE.role.web.response.RoleResponse;
import com.esgi.framework_JEE.role.web.resquest.RoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleCommand roleCommand;

    @Autowired
    RoleQuery roleQuery;

    public RoleController(RoleCommand roleCommand, RoleQuery roleQuery) {
        this.roleCommand = roleCommand;
        this.roleQuery = roleQuery;
    }

    @RequestMapping("/create")
    public ResponseEntity<RoleResponse> addRole(@RequestBody RoleRequest roleRequest){
        var role = roleCommand.create(roleRequest);
        if(role == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(roleToRoleResponse(role), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<RoleResponse>> getAll(){
        return new ResponseEntity<>(
                this.listRoleToListRoleResponse(roleQuery.getAll()),
                HttpStatus.OK);
    }

    //to test
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable int roleId){
        var role = roleQuery.getById(roleId);
        if(role == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(roleToRoleResponse(
                role),
                HttpStatus.OK
        );
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String roleName){
        return new ResponseEntity<>(roleToRoleResponse(
                roleQuery.getByName(roleName)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<?> changeContent(@PathVariable int roleId,@RequestBody RoleRequest roleRequest){
        var role = roleCommand.changeName(roleRequest,roleId);
        if(role == null)
            return new ResponseEntity<>("Invalid properties", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(roleToRoleResponse(role), HttpStatus.OK);
    }


    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable int roleId){
        var role = roleQuery.getById(roleId);
        if(role == null)
            return new ResponseEntity<>(
                    "Role " + roleId + " not exist",
                    HttpStatus.BAD_REQUEST
            );
        roleCommand.delete(roleId);
        return new ResponseEntity<>(
                "Role " + roleId + " deleted",
                HttpStatus.OK
        );
    }






    private RoleResponse roleToRoleResponse(Role role){
        return new RoleResponse()
                .setId(role.getId())
                .setName(role.getTitlePermission());
    }

    private List<RoleResponse> listRoleToListRoleResponse(List<Role> roles){
        List<RoleResponse> roleResponses = new ArrayList<>();
        roles.forEach(role -> roleResponses.add(this.roleToRoleResponse(role)));
        return roleResponses;
    }
}
