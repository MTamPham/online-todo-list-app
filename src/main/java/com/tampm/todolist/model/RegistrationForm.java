package com.tampm.todolist.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class RegistrationForm {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;

    @NotBlank
    private final String confirm;

    @NotBlank
    private final String fullName;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(
            username, passwordEncoder.encode(password), fullName
        );
    }
}
