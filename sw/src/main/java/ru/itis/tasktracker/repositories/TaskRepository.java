package ru.itis.tasktracker.repositories;

import ru.itis.tasktracker.model.Task;
import ru.itis.tasktracker.repositories.generic.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task,Integer> {
    List<Task> findByProjectId(Integer id);
}
