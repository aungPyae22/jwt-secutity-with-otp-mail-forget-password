package com.aungPyae22.forgot_mail_sender.auth.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;

    @NotBlank(message = "please provide first name..")
    private String firstName;

    @NotBlank(message = "please provide last name..")
    private String lastName;

    @NotBlank(message = "please provide username..")
    private String username;

    @NotBlank(message = "please provide email..")
    private String email;

    @NotBlank(message = "please provide role")
    private String role;
}
