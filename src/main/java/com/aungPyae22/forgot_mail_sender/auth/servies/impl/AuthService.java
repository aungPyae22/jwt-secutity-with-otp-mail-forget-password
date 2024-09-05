package com.aungPyae22.forgot_mail_sender.auth.servies.impl;

import com.aungPyae22.forgot_mail_sender.auth.entities.Otp;
import com.aungPyae22.forgot_mail_sender.auth.entities.Role;
import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import com.aungPyae22.forgot_mail_sender.auth.mapping.Mapping;
import com.aungPyae22.forgot_mail_sender.auth.repositories.OtpRepository;
import com.aungPyae22.forgot_mail_sender.auth.repositories.UserRepository;
import com.aungPyae22.forgot_mail_sender.auth.requests.LoginRequest;
import com.aungPyae22.forgot_mail_sender.auth.responses.UserDto;
import com.aungPyae22.forgot_mail_sender.auth.servies.interfaces.IAuthService;
import com.aungPyae22.forgot_mail_sender.auth.servies.interfaces.IOtpService;
import com.aungPyae22.forgot_mail_sender.dto.Response;
import com.aungPyae22.forgot_mail_sender.exceptions.FieldRequireException;
import com.aungPyae22.forgot_mail_sender.exceptions.UserExistsException;
import com.aungPyae22.forgot_mail_sender.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService implements IAuthService, IOtpService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    private final OtpRepository otpRepository;
    private final long OTP_VALIDATION_DURATION = 1000 * 60 * 10; //10 Minutes

    @Autowired
    public AuthService(UserRepository userRepository,
                       AuthenticationManager authManager,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       JavaMailSender javaMailSender,
                       OtpRepository otpRepository){

        this.userRepository = userRepository;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.javaMailSender = javaMailSender;
        this.otpRepository = otpRepository;
    }

    @Override
    public Response register(User user) {
        Response response = new Response();
        try{
            if(user == null)
                throw new FieldRequireException("Please Provide require field");
            if(userRepository.existsByEmail(user.getEmail()))
                throw new UserExistsException("email is already exists , please provide another email..");
            if(user.getRole() == null){
                user.setRole(Role.USER);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User registerUser = userRepository.save(user);
            UserDto userDto = Mapping.mappingUserEntityToUserDto(registerUser);
            response.setStatus(200);
            response.setMessage("successfully registered..");
            response.setUserDto(userDto);
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the register..");
        }
        return response;
    }

    @Override
    public Response login(LoginRequest request) {
        Response response = new Response();
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

            User user = userRepository.findByEmail(request.getEmail());
            if(user == null ) throw new UserNotFoundException("User could found in server..");

            String token = jwtService.generateToken(user);

            response.setStatus(200);
            response.setMessage("successfully login..");
            response.setToken(token);
            response.setExpirationToken("7 day");
            response.setRole(user.getRole().name());
        }
        catch(UserNotFoundException e){
            response.setStatus(400);
            response.setMessage("UserNotFound Exception" + e.getMessage());
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the logged in.." + e.getMessage());
        }
        return response;
    }


    @Override
    public void generateOtp(String email) {
            User user = userRepository.findByEmail(email);
            if(user == null) throw new UserNotFoundException("please provide the valid email..");

            user.setOtp(new Otp(user.getId(),Mapping.OtpGenerator(),new Date(System.currentTimeMillis() + OTP_VALIDATION_DURATION)));
            userRepository.save(user);

            sendEmail(user);
    }

    public boolean validateOTPAndNewPassword(String email,Integer otp, String newPassword){
        User user = userRepository.findByEmail(email);
        if(user == null || newPassword == null){
            return false;
        }

        if(user.getOtp().getOtpCode().equals(otp) &&
            user.getOtp().getExpirationOtp().after(new Date(System.currentTimeMillis()))){
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    private void sendEmail(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mgmgzues8@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code for forgot password :" + user.getOtp().getOtpCode());
        javaMailSender.send(message);
    }
}
