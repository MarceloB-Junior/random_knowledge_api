package com.api.random_knowledge.configs;

import com.api.random_knowledge.enums.RoleEnum;
import com.api.random_knowledge.models.UserModel;
import com.api.random_knowledge.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class DefaultAdminConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if(!userRepository.existsByEmail("john.doe@example.com")){
            var defaultAdmin = new UserModel();
            defaultAdmin.setName("John Doe");
            defaultAdmin.setPassword(passwordEncoder.encode("pwd123"));
            defaultAdmin.setEmail("john.doe@example.com");
            defaultAdmin.setRole(RoleEnum.ADMIN);

            var savedAdmin = userRepository.save(defaultAdmin);
            log.info("Default admin account created with email: {}", savedAdmin.getEmail());
        }else {
            log.info("Default admin account already exists");
        }

    }
}