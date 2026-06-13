package main.store.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.HttpSecurityDslKt;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
            .csrf(csrf -> csrf.disable()/*csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())*/)

            .authorizeHttpRequests(auth ->
                        auth
                            .requestMatchers(HttpMethod.POST, "/registration").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                            .requestMatchers("/cart/**", "/orders/**").hasRole("USER")

                            .requestMatchers("/admin/**").hasRole("ADMIN")

                            .anyRequest().authenticated()

                ).formLogin(form -> form
                        .loginPage("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")

                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                        })

                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"error\": \"Unsuccess enter\", \"incorrect email or password\"}");
                        })
                        .permitAll()


                ).logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"error\": \"Required authorization\"}");
                        })
                );
        return http.build();

    }
}
