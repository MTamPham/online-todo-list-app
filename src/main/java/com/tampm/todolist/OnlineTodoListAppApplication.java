package com.tampm.todolist;

import com.tampm.todolist.model.User;
import com.tampm.todolist.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OnlineTodoListAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTodoListAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				userRepo.save(new User("test", passwordEncoder.encode("pwd123"), "Test"));
			}
		};
	}
}
