package com.taulia.invoice.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfiguration {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
              .requestMatchers(HttpMethod.GET, "/invoice/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/buyer/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/supplier/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/invoice").authenticated()
              .requestMatchers(HttpMethod.POST, "/buyer").authenticated()
              .requestMatchers(HttpMethod.POST, "/supplier").authenticated()
              .requestMatchers(HttpMethod.PATCH, "/invoice/**").authenticated()
              .requestMatchers(HttpMethod.DELETE, "/invoice/**").authenticated();
        })
        .httpBasic(withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
    UserDetails user = User.withUsername("alex").password(passwordEncoder().encode("123")).build();
    userDetailsService.createUser(user);
    return userDetailsService;
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
