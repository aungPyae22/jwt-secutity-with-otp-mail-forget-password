package com.aungPyae22.forgot_mail_sender.controllers;

import com.aungPyae22.forgot_mail_sender.dto.Response;
import com.aungPyae22.forgot_mail_sender.exceptions.FieldRequireException;
import com.aungPyae22.forgot_mail_sender.exceptions.UserNotFoundException;
import com.aungPyae22.forgot_mail_sender.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/securities/information")
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        Response response = iUserService.getAllUser();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = "/users/usernames")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> getAllUsername(){
        Response response = iUserService.getAllUsername();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = "/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") long id){
        Response response = iUserService.getUserById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping(path = "/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateUser(@RequestPart long id,
                                               @RequestPart String firstName,
                                               @RequestPart String lastName,
                                               @RequestPart String username,
                                               @RequestPart String email,
                                               @RequestPart String password,
                                               @RequestPart String role){

        if(firstName.isBlank() ||
                lastName.isBlank() ||
                username.isBlank() ||
                email.isBlank() ||
                password.isBlank() ||
                role.isBlank()){
            throw new FieldRequireException("Please provide require field exactly..");
        }

        Response response = iUserService.updateUser(id,
                firstName,
                lastName,
                username,
                email,
                password,
                role);

        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @DeleteMapping(path = "/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") long id){
        Response response = iUserService.deleteUserById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(path = "/users/get-my-info")
    public ResponseEntity<Response> getMyInformation(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myEmail = authentication.getName();

        if(myEmail == null) throw new UserNotFoundException("error during the getting your information..");

        Response response = iUserService.getMyInfo(myEmail);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
