package com.tampm.todolist.controller;

import com.tampm.todolist.model.Todo;
import com.tampm.todolist.model.User;
import com.tampm.todolist.repository.TodoRepository;
import com.tampm.todolist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class TodoControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    TodoRepository todoRepo;

    @MockBean
    private UserRepository userRepo;

    private User user;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new User("testuser", "testpass", "Test User");
        user.setId(1L);
        when(userRepo.findByUsername("testuser"))
                .thenReturn(user);
    }

    @Test
    public void testSplash_shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"))
                .andExpect(content().string(containsString("Sign Up")));
    }

    @Test
    @WithMockUser(username="testuser", password="testpass")
    public void testShowTodoList() throws Exception {
        when(todoRepo.findByUser(user))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/todo/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("todoList"));
    }

//    @Test
//    @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
//    public void testGetTask_shouldDisplay() throws Exception {
//        Todo todo = new Todo();
//        todo.setId(1L);
//        todo.setTitle("Dummy title");
//        todo.setDescription("Dummy description");
//        todo.setCreatedAt(new Date());
//        todo.setModifiedAt(new Date());
//        when(todoRepo.findById(any())).thenReturn(Optional.of(todo));
//
//        mockMvc.perform(get("/todo/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("todo"))
//                .andExpect(content().string(containsString("Dummy title")));
//    }

//    @Test
//    @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
//    public void testSaveTask_shouldRedirect() throws Exception {
//        Todo todo = new Todo();
//        todo.setId(1L);
//        todo.setTitle("title 1");
//        todo.setDescription("description 1");
//        when(todoRepo.save(todo))
//                .thenReturn(todo);
//        when(todoRepo.save(todo)).thenReturn(todo);
//
//        mockMvc.perform(post("/todo/save")
//                        .content("title=task+1&description=description+1")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(header().stringValues("Location", "/todo/list"));
//    }

    @Test
    @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
    public void testSaveTask_shouldFail() throws Exception {
        mockMvc.perform(post("/todo/save").with(csrf())
                        .content("title=&description=")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title must be at least")));
    }
}
