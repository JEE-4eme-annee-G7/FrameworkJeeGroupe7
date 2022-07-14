package com.esgi.framework_JEE.user.web.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.esgi.framework_JEE.user.command.UserCommand;
import com.esgi.framework_JEE.user.Domain.entities.User;
import com.esgi.framework_JEE.user.query.UserQuery;
import com.esgi.framework_JEE.user.web.request.UserRequest;
import com.esgi.framework_JEE.user.web.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    final UserCommand userCommand;
    final UserQuery userQuery;

    public UserController(UserCommand userCommand, UserQuery userQuery){
        this.userCommand = userCommand;
        this.userQuery = userQuery;
    }

    @PostMapping("/create")
    public  ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        if(userQuery.userEmailExist(userRequest.email))
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        var createdUser = userCommand.create(userRequest);
        if(createdUser == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getByEmail(@PathVariable String email){
        var user = userQuery.getByEmail(email);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(userToUserResponse(user),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/", produces = { MimeTypeUtils.APPLICATION_JSON_VALUE }, headers = "Accept=application/json")
    public ResponseEntity<List<UserResponse>> getUserAll(){
        return new ResponseEntity<>(
                listUserToListUserResponse(userQuery.getAll()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = { MimeTypeUtils.APPLICATION_JSON_VALUE }, headers = "Accept=application/json")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int userId) {
        var user = userQuery.getById(userId);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(
                user),
                HttpStatus.OK
        );
    }

    /*@GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest){
        var user = userQuery.getByEmailAndPassword(userRequest.email, userRequest.password);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(user), HttpStatus.OK);
    }*/

    @PatchMapping("/password/{userId}")
    public ResponseEntity<UserResponse> changePassword(@PathVariable int userId, @RequestBody UserRequest userRequest){
        var userNewPassword = userCommand.changePassword(userId, userRequest);
        if(userNewPassword == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(userNewPassword), HttpStatus.OK);
    }

    @PatchMapping("/email/{userId}")
    public ResponseEntity<UserResponse> changeEmail(@PathVariable int userId, @RequestBody UserRequest userRequest){
        var user = userCommand.changeEmail(userId, userRequest);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(user), HttpStatus.OK);

    }

    @PatchMapping("/lastname/{userId}")
    public ResponseEntity<UserResponse> changeLastname(@PathVariable int userId, @RequestBody UserRequest userRequest){
        var user = userCommand.changeLastname(userId, userRequest);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(user), HttpStatus.OK);
    }

    @PatchMapping("/firstname/{userId}")
    public ResponseEntity<UserResponse> changeFirstname(@PathVariable int userId, @RequestBody UserRequest userRequest){
        var user = userCommand.changeFirstname(userId, userRequest);
        if(user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userToUserResponse(user), HttpStatus.OK);
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        var user = userQuery.getById(userId);
        if(user == null)
            return new ResponseEntity<>(
                    "User " + userId + " not exist",
                    HttpStatus.BAD_REQUEST
            );
        userCommand.delete(userId);
        return new ResponseEntity<>(
                "User " + userId + " deleted",
                HttpStatus.ACCEPTED
        );

    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userQuery.getByEmail(username);
                String access_token = JWT.create()
                        .withSubject(user.getFirstname())
                        .withExpiresAt(new java.sql.Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getPermission().getTitlePermission())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> errors = new HashMap<>();
                errors.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }



    private UserResponse userToUserResponse(User user){
        return new UserResponse()
                .setId(user.getId())
                .setFirstname(user.getFirstname())
                .setEmail(user.getEmail())
                .setLastname(user.getLastname());
    }

    private List<UserResponse> listUserToListUserResponse(List<User> users){
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user -> userResponses.add(userToUserResponse(user)));
        return userResponses;
    }
}
