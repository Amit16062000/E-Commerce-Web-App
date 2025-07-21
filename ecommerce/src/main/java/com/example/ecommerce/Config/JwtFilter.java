package com.example.ecommerce.Config;

import com.example.ecommerce.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtFilter {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtConfig jwtConfig, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtConfig, userDetailsService);
    }
}
