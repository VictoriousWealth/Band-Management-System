package uk.ac.sheffield.team28.team28.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/auth/register", "/auth/login", "/", "/js/registrationFormJS.js").permitAll() // Public access to register and login
                .anyRequest().authenticated()                       // Require authentication for all other endpoints
            )
            .formLogin((form) -> form
                .loginProcessingUrl("/auth/login")                       // URL to submit login data
                .loginPage("/auth/login")                                // Custom login page URL
                .defaultSuccessUrl("/dashboard")                    // Redirect to /dashboard on successful login
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
