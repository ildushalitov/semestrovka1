package ru.itis.tasktracker.repositories;

import ru.itis.tasktracker.model.Comment;
import ru.itis.tasktracker.repositories.generic.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findByTaskId(Integer id);
}
