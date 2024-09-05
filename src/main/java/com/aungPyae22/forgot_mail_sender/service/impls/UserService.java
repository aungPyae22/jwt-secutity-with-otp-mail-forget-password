package com.aungPyae22.forgot_mail_sender.service.impls;

import com.aungPyae22.forgot_mail_sender.auth.entities.Role;
import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import com.aungPyae22.forgot_mail_sender.auth.mapping.Mapping;
import com.aungPyae22.forgot_mail_sender.auth.repositories.UserRepository;
import com.aungPyae22.forgot_mail_sender.auth.responses.UserDto;
import com.aungPyae22.forgot_mail_sender.dto.Response;
import com.aungPyae22.forgot_mail_sender.exceptions.UserNotFoundException;
import com.aungPyae22.forgot_mail_sender.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response getAllUser() {
        Response response = new Response();
        try{
            List<User> userList = userRepository.findAll();
            List<UserDto> userDtoList = Mapping.mappingUserListEntityToUserListDto(userList);

            response.setStatus(200);
            response.setMessage("successfully got all users..");
            response.setUserDToList(userDtoList);
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the getting all users..");
        }
        return response;
    }

    @Override
    public Response getUserById(Long id) {
        Response response = new Response();
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User cannot found in this server.please find with valid id"));
            UserDto userDto = Mapping.mappingUserEntityToUserDto(user);

            response.setStatus(200);
            response.setMessage("successfully got user by id..");
            response.setUserDto(userDto);
        }
        catch (UserNotFoundException e){
            response.setStatus(400);
            response.setMessage("user cannot found in server by id " + e.getMessage());
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the getting user by id "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateUser(Long id, String firstName, String lastName, String username, String email, String password, String role) {
        Response response = new Response();
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user could not found in sever.."));

            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.valueOf(role));

            userRepository.save(user);

            response.setStatus(200);
            response.setMessage("successfully updated user information..");
        }
        catch(UserNotFoundException e){
            response.setStatus(400);
            response.setMessage("User could not found in sever..Please provide the valid id "+e.getMessage());
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the updating user.." + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUserById(Long id) {
        Response response = new Response();

        try{
            userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user could not found in server.."));
            userRepository.deleteById(id);

            response.setStatus(200);
            response.setMessage("successfully deleted user..");
        }
        catch(UserNotFoundException e){
            response.setStatus(400);
            response.setMessage("user could not found in server..");
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the deleting user.." + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsername() {
        Response response = new Response();
        try{
            List<String> usernameList = userRepository.getAllUsername();

            response.setStatus(200);
            response.setMessage("successfully got all usernames..");
            response.setUsernameList(usernameList);
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the getting usernames "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email);
            if(user == null) throw new UserNotFoundException("user not found in server..");
            UserDto userDto = Mapping.mappingUserEntityToUserDto(user);

            response.setStatus(200);
            response.setMessage("successfully got your information..");
            response.setUserDto(userDto);
        }
        catch(UserNotFoundException e){
            response.setStatus(400);
            response.setMessage("user information could not found in sever.."+e.getMessage());
        }
        catch(Exception e){
            response.setStatus(500);
            response.setMessage("Error occurs during the getting you information.."+e.getMessage());
        }
        return response;
    }
}
