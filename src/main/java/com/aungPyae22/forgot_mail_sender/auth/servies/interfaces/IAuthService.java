package com.aungPyae22.forgot_mail_sender.auth.servies.interfaces;

import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import com.aungPyae22.forgot_mail_sender.auth.requests.LoginRequest;
import com.aungPyae22.forgot_mail_sender.dto.Response;

public interface IAuthService {

    Response register(User user);
    Response login(LoginRequest request);
}
