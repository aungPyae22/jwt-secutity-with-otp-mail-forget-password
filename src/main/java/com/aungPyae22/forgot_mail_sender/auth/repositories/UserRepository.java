package com.aungPyae22.forgot_mail_sender.auth.repositories;

import com.aungPyae22.forgot_mail_sender.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

     User findByEmail(String email);

     boolean existsByEmail(String email);

     @Query("SELECT DISTINCT u.username FROM User u")
     List<String> getAllUsername();

}
