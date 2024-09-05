package com.aungPyae22.forgot_mail_sender.auth.repositories;

import com.aungPyae22.forgot_mail_sender.auth.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long> {

}
