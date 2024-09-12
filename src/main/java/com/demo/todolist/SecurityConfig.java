package com.demo.todolist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author cash.wu
 * @since 2024/09/12
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public UserDetailsService userDetailsService() {

//        debug only
//        String userEncodePw = passwordEncoder().encode("user123!");
//        log.info("userEncodePw: {}", userEncodePw);

        UserDetails user = User.builder()
                               .username("user")
                               .password(passwordEncoder().encode("user123!"))
                               .roles("USER")
                               .build();

        UserDetails admin = User.builder()
                                .username("admin")
                                .password(passwordEncoder().encode("admin123!"))
                                .roles("ADMIN", "USER")
                                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
