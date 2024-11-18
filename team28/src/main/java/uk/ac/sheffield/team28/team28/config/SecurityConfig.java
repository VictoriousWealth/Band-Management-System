package uk.ac.sheffield.team28.team28.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import uk.ac.sheffield.team28.team28.service.CustomUserDetailsService;
import uk.ac.sheffield.team28.team28.security.CustomAuthenticationFailureHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/auth/register", "/auth/login", "/").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginProcessingUrl("/auth/login")
                .loginPage("/auth/login")
                .defaultSuccessUrl("/dashboard")                // Redirect to /dashboard on successful login
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/auth/logout")                               // URL to trigger logout
                .logoutSuccessUrl("/")                         // Redirect to /login after logout
                .invalidateHttpSession(true)                        // Invalidate session on logout
                .clearAuthentication(true)                          // Clear authentication details on logout
                .permitAll()
            )
             .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();


}

}