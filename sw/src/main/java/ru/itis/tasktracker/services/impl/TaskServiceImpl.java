package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.TaskForm;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Task;
import ru.itis.tasktracker.repositories.TaskRepository;
import ru.itis.tasktracker.services.TaskMapper;
import ru.itis.tasktracker.services.TaskService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor

public class TaskServiceImpl implements TaskService {
    TaskRepository taskRepository;
    TaskMapper taskMapper;

    @Override
    public TaskDto save(TaskForm form, Integer projectId) {
        Task task = taskMapper.toTask(form);
        task.setCreatedAt(LocalDateTime.now());
        task.setProjectId(projectId);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto update(TaskForm form) {
        Task task = taskMapper.toTask(form);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> getTasks(Integer id) {
        return taskRepository.findByProjectId(id)
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTask(String taskId) {
        try {
            if (taskId.isEmpty()) throw new TaskTrackerException("Cannot find this task");
            Integer id = Integer.parseInt(taskId);
            Optional<Task> task = taskRepository.findById(id);
            if (task.isEmpty()) {
                throw new TaskTrackerException("Cannot find a project");
            }
            return taskMapper.toDto(task.get());
        } catch (SQLException e) {
            throw new TaskTrackerException("Cannot find a task");
        }
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new TaskTrackerException("No task id provided");
        }
        taskRepository.delete(Integer.parseInt(id));

    }
}
