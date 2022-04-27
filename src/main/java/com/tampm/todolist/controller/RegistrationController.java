package com.tampm.todolist.controller;

import com.tampm.todolist.model.RegistrationForm;
import com.tampm.todolist.model.User;
import com.tampm.todolist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        // the encoder bean is created when the WebSecurityConfig is initialized
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid RegistrationForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // Verify it the same username exists
        User dbUser = userRepo.findByUsername(form.getUsername());
        if (dbUser != null) {
            model.addAttribute("duplicateError", "Sorry, this username is already taken.");
            return "registration";
        }

        // Verify it password and confirm password matches
        if (!form.getPassword().equals(form.getConfirm())) {
            model.addAttribute("notMatchError", "Sorry, your password and your confirm is not matched.");
            return "registration";
        }

        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
