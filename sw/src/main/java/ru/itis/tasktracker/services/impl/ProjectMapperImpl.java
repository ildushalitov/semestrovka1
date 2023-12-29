package ru.itis.tasktracker.services.impl;

import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.services.ProjectMapper;

public class ProjectMapperImpl implements ProjectMapper {
    public ProjectDto toDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .description(project.getDescription())
                .name(project.getName())
                .createdAt(project.getCreatedAt())
                .ownerId(project.getOwnerId())
                .build();
    }

    public Project toProject(ProjectDto dto){
        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public Project toProject(ProjectForm dto){
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

}
