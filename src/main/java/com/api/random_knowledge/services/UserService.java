package com.api.random_knowledge.services;

import com.api.random_knowledge.dtos.responses.UserResponse;
import com.api.random_knowledge.enums.RoleEnum;
import com.api.random_knowledge.models.UserModel;
import com.api.random_knowledge.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse save(UserModel userModel){
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.setRole(RoleEnum.USER);

        var userSaved = userRepository.save(userModel);
        log.debug("User saved successfully: {}", userSaved.getEmail());

        return UserResponse.builder()
                .name(userSaved.getName())
                .email(userSaved.getEmail())
                .build();
    }

    public boolean existsByEmail(String email){
        log.debug("Checking existence of user with email: {}", email);
        return userRepository.existsByEmail(email);
    }

}