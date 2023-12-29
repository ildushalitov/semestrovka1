package ru.itis.tasktracker.repositories;

import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.repositories.generic.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    List<Project> findByUserId(Integer id);


}
