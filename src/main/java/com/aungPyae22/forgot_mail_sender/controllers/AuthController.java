package com.aungPyae22.forgot_mail_sender.controllers;

import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import com.aungPyae22.forgot_mail_sender.auth.requests.LoginRequest;
import com.aungPyae22.forgot_mail_sender.auth.servies.impl.AuthService;
import com.aungPyae22.forgot_mail_sender.dto.Response;
import com.aungPyae22.forgot_mail_sender.exceptions.FieldRequireException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/securities/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Response> register(@RequestBody User user){
        if(user == null) throw new FieldRequireException("Please provide require field exactly..");
        Response response = authService.register(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request){
        if(request.getEmail().isBlank() || request.getPassword().isBlank())
            throw new FieldRequireException("please provide the require email and password");
        Response response = authService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping(path = "/forgot-password")
    public String forgotPassword(@RequestParam String email){
        if(email.isBlank())
            throw new FieldRequireException("please provide require email to get otp..");
        authService.generateOtp(email);

        return "OTP had been sand to your email.It it work during the 10 minutes";
    }

    @PostMapping(path = "/change-password")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String otp,
                                 @RequestParam String newPassword){

        if(email.isBlank() || newPassword.isBlank())
            throw new FieldRequireException("please provide require field email,otp,new password exactly");

        boolean isValid = authService.validateOTPAndNewPassword(email, Integer.valueOf(otp), newPassword);

        if(isValid){
            return "OTP is valid and successfully updated password";
        }else{
            return "OTP is invalid";
        }

    }


}
