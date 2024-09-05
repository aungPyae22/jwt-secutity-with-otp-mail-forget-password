package com.aungPyae22.forgot_mail_sender.dto;

import com.aungPyae22.forgot_mail_sender.auth.responses.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status;
    private String message;

    private String token;
    private String expirationToken;
    private String role;
    private List<String> usernameList;

    private UserDto userDto;
    private List<UserDto> userDToList;
}
