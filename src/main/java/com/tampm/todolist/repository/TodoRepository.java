package com.tampm.todolist.repository;

import com.tampm.todolist.model.Todo;
import com.tampm.todolist.model.TodoStatistic;
import com.tampm.todolist.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    List<Todo> findByUser(User user);

    @Transactional
    @Query(value = "SELECT * FROM Todo t WHERE t.user_id = :#{#user.id} AND t.is_complete = :completeStatus",
            nativeQuery = true)
    List<Todo> findByUserAndStatus(@Param("user") User user, @Param("completeStatus") boolean completeStatus);

    Optional<Todo> findByIdAndUser(long id, User user);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Todo t SET is_complete = :completeStatus, modified_at = CURRENT_TIMESTAMP WHERE t.id = :id",
            nativeQuery = true)
    void updateTodo(@Param("id") Long id, @Param("completeStatus") boolean completeStatus);
}
