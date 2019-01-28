package com.mejner.projectmanagertool.services;

import com.mejner.projectmanagertool.domain.User;
import com.mejner.projectmanagertool.exceptions.UsernameAlreadyExistException;
import com.mejner.projectmanagertool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());

            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameAlreadyExistException("Uzytkownik '" + newUser.getUsername() + "' juz istnieje");
        }

    }
}
