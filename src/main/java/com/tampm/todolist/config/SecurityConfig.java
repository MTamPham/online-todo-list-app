package com.tampm.todolist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // configure URL paths required authentication and URL path not required authentication
            .authorizeRequests()
            // ensure that requests for /list, /new, /{id}, /save, /delete/{id} are only available to authenticated users
            .antMatchers("/todo/**")
            // allow access if the given SpEL expression evaluates to true
            .access("hasRole('ROLE_USER')")
            // all other requests should be permitted for all users
            .antMatchers("/", "/**")
            .access("permitAll")
            // custom login page
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/todo/list")
            // enable log out
            .and()
            .logout()
            .logoutSuccessUrl("/")
            // make H2-Console non-secured; for debug purposes
            .and()
            .csrf()
            .ignoringAntMatchers("/h2-console/**")
            // Allow pages to be loaded in frames from the same origin; needed for H2-Console
            .and()
            .headers()
            .frameOptions()
            .sameOrigin();
    }
}