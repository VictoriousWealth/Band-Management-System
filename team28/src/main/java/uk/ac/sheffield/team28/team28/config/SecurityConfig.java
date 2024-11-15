package uk.ac.sheffield.team28.team28.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import uk.ac.sheffield.team28.team28.service.CustomUserDetailsService;

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
                .requestMatchers("/auth/register", "/auth/login", "/", "/css/**", "/js/**").permitAll() // Public access to register and login
                .anyRequest().authenticated()                       // Require authentication for all other endpoints
            )
            .formLogin((form) -> form
                .loginProcessingUrl("/auth/login")                       // URL to submit login data
                .loginPage("/auth/login")                                // Custom login page URL
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/dashboard", true)  // Redirect to /dashboard on successful login
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/auth/logout")                               // URL to trigger logout
                .logoutSuccessUrl("/auth/login?logout=true")                         // Redirect to /login after logout
                .invalidateHttpSession(true)                        // Invalidate session on logout
                .clearAuthentication(true)                          // Clear authentication details on logout
                .permitAll()
            )
            .authenticationProvider(authenticationProvider());


        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.withUsername("test@example.com")
                .password(passwordEncoder().encode("TestPassword123"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
