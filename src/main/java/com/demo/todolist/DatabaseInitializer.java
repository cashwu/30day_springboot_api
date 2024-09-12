package com.demo.todolist;

import com.demo.todolist.model.User;
import com.demo.todolist.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void initDatabase() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123!"));
            user.setRoles("USER");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123!"));
            admin.setRoles("ADMIN,USER");

            userRepository.saveAll(Arrays.asList(user, admin));

            log.info("Default users initialized successfully");
        }
    }
}
