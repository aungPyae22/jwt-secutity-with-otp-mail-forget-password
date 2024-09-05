package com.aungPyae22.forgot_mail_sender.auth.servies.interfaces;

import com.aungPyae22.forgot_mail_sender.dto.Response;

public interface IOtpService {

    void generateOtp(String email);
}
