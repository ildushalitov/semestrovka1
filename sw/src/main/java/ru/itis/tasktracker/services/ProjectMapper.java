package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.model.Project;

public interface ProjectMapper {
    ProjectDto toDto(Project project);


    Project toProject(ProjectDto dto);
    Project toProject(ProjectForm dto);
}
