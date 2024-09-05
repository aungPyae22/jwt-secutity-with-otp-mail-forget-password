package com.aungPyae22.forgot_mail_sender.service.interfaces;

import com.aungPyae22.forgot_mail_sender.dto.Response;

public interface IUserService {

    Response getAllUser();

    Response getUserById(Long id);

    Response updateUser(Long id, String firstName, String lastName, String username, String email, String password, String role);

    Response deleteUserById(Long id);

    Response getAllUsername();

    Response getMyInfo(String email);
}
