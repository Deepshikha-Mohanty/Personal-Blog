package projects.personalblog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF ONLY for APIs
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                // Allow APIs without login
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                )

                // KEEP form login
                .formLogin(form -> form
                        .loginPage("/login") // optional (Spring default works too)
                        .permitAll()
                )

                // optional
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}