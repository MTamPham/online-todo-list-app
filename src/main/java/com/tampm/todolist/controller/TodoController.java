package com.tampm.todolist.controller;

import com.tampm.todolist.exception.ResourceNotFoundException;
import com.tampm.todolist.model.Todo;
import com.tampm.todolist.model.TodoStatistic;
import com.tampm.todolist.model.User;
import com.tampm.todolist.repository.TodoRepository;
import com.tampm.todolist.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {
    private static final Logger log = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    TodoRepository todoRepo;

    @Autowired
    UserRepository userRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping(path = "/list")
    public String listTasks(Model model, Principal principal, @RequestParam(required = false) Boolean status) {
        User user = userRepo.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Sorry, we couldn't verify your account");
        }

        // Get statistic info
//        Query query = entityManager.createNativeQuery("SELECT COUNT(*) AS `all`" +
//                ", COALESCE(SUM(CASEWHEN(is_complete = true, 1, 0)),0) AS `complete`" +
//                ", COALESCE(SUM(CASEWHEN(is_complete = false, 1, 0)),0) AS `incomplete` " +
//                "FROM Todo t WHERE t.user_id = ?1");
//        List<Object[]> result = query.getResultList();
//
//        TodoStatistic statistic = (TodoStatistic) result;
//        model.addAttribute("totalAll", statistic.getAll());
//        model.addAttribute("totalComplete", statistic.getComplete());
//        model.addAttribute("totalInComplete", statistic.getIncomplete());

        List<Todo> todoList;
        if (status != null) {
            todoList = todoRepo.findByUserAndStatus(user, status);
        } else {
            todoList = todoRepo.findByUser(user);
        }

        model.addAttribute("todoList", todoList);
        model.addAttribute("status", status);

        return "todoList";
    }

    @GetMapping(path = "/new")
    public String newTask(Model model) {
        model.addAttribute("todo", new Todo());

        return "todo";
    }

    @GetMapping(path = "/{id}")
    public String getTask(@PathVariable Long id,  Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Sorry, we couldn't verify your account");
        }

        Todo todo = todoRepo.findByIdAndUser(id, user).orElse(null);
        if (todo == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Sorry, we couldn't find the TODO you gave us");
        }

        model.addAttribute("todo", todo);

        return "todo";
    }

    @PostMapping(path = "/save")
    public String saveTask(@Valid Todo todo, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "todo";
        }

        User user = userRepo.findByUsername(principal.getName());
        if (user == null) {
            throw new ResourceNotFoundException("Sorry, we couldn't verify your account");
        }

        // Verify if this task belongs to this user
        if (todo.getId() != null) {
            Todo dbTodo = todoRepo.findByIdAndUser(todo.getId(), user).orElse(null);
            if (dbTodo == null) {
                throw new ResourceNotFoundException("Sorry, we couldn't find the TODO you gave us");
            }
        }
        todo.setUser(user);

        Todo saved = todoRepo.save(todo);
        if (saved == null || saved.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, we couldn't store the TODO you gave us");
        }
        log.info("Saved to Todo ID: " + saved.getId());

        return "redirect:/todo/list";
    }

    @PostMapping(path = "/complete")
    public String completeTask(@RequestParam Long id, @RequestParam(required = false) boolean completeStatus) {
        todoRepo.updateTodo(id, completeStatus);
        log.info(String.format("Updated Todo ID: %d with status: %b", id, completeStatus));

        return "redirect:/todo/list";
    }

    @PostMapping(path = "/delete")
    public String deleteTask(@RequestParam Long id, Principal principal) {
        if (!todoRepo.existsById(id)) {
            throw new ResourceNotFoundException("Couldn't find Task with ID " + id);
        }

        User user = userRepo.findByUsername(principal.getName());

        // Verify if this task belongs to this user
        Todo dbTodo = todoRepo.findByIdAndUser(id, user).orElse(null);
        if (dbTodo == null) {
            throw new ResourceNotFoundException("Sorry, we couldn't find the TODO you gave us");
        }

        todoRepo.deleteById(id);
        log.info("Deleted Todo ID: " + id);

        return "redirect:/todo/list";
    }

    @ModelAttribute
    public void addUserToModel(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
    }


}
