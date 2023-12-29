package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.model.User;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getUsersProjects(UserDto user);
    ProjectDto save(ProjectForm form, UserDto user);
    ProjectDto getProject(Integer id);

    ProjectDto update(ProjectDto form);

    void delete(Integer id);
}
