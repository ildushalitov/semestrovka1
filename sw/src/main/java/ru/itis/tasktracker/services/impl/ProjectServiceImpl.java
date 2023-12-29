package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;
import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Project;

import ru.itis.tasktracker.repositories.ProjectRepository;
import ru.itis.tasktracker.services.ProjectMapper;
import ru.itis.tasktracker.services.ProjectService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    ProjectMapper projectMapper;

    public ProjectDto save(ProjectForm form, UserDto user) {
        if (form.getName() == null) {
            throw new TaskTrackerException("Project must nave name");
        }
        Project project = projectMapper.toProject(form);
        project.setOwnerId(user.getId());
        project.setCreatedAt(LocalDateTime.now());
        projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    public List<ProjectDto> getUsersProjects(UserDto user) {
        return projectRepository.findByUserId(user.getId())
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProject(Integer id) {
        try {
            return projectMapper.toDto(projectRepository.findById(id).get());
        } catch (SQLException e) {
            throw new TaskTrackerException("Cannot find a project");
        }
    }

    @Override
    public ProjectDto update(ProjectDto form) {
        try {
            Project existingProject = projectRepository.findById(form.getId())
                    .orElseThrow(() -> new TaskTrackerException("Project not found"));
            if (form.getId() == null || form.getName() == null) {
                throw new TaskTrackerException("Project ID and name are required for updating");
            }
            existingProject.setName(form.getName());
            existingProject.setDescription(form.getDescription());

            projectRepository.save(existingProject);

            return projectMapper.toDto(existingProject);
        } catch (SQLException e) {
            throw new TaskTrackerException("Project does not exist :(");
        }
    }

    @Override
    public void delete(Integer id) {
        projectRepository.delete(id);
    }
}
