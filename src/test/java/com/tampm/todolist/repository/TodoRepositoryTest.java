package com.tampm.todolist.repository;

import com.tampm.todolist.config.SecurityConfig;
import com.tampm.todolist.model.Todo;
import com.tampm.todolist.model.User;
import com.tampm.todolist.service.UserDetailsService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import({SecurityConfig.class})
public class TodoRepositoryTest {
//    @Autowired
//    private TodoRepository todoRepo;
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Before
//    public void setup() throws Exception {
//        userRepo.save(new User("testuser", "testpwd", "Test User"));
//    }
//
//    @Test
//    public void testSaved_thenFindById() throws Exception {
//        User user = userRepo.findByUsername("testuser");
//
//        Todo todo = new Todo();
//        todo.setTitle("Dummy title");
//        todo.setUser(user);
//        Todo saved = todoRepo.save(todo);
//        assertThat(todoRepo.findById(saved.getId()), notNullValue());
//    }
}
