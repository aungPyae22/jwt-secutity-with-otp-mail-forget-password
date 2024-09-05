package com.aungPyae22.forgot_mail_sender.auth.mapping;

import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import com.aungPyae22.forgot_mail_sender.auth.responses.UserDto;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Mapping {

    //To get userDto from userEntity
    public static UserDto mappingUserEntityToUserDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    //To get userDto list from userEntity list
    public static List<UserDto> mappingUserListEntityToUserListDto(List<User> userList){
        return userList.stream().map(
                Mapping::mappingUserEntityToUserDto).collect(Collectors.toList());
    }

    //To generate Otp
    public static Integer OtpGenerator(){
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }
}
