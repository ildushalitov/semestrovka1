package ru.itis.tasktracker.services.impl;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.TaskForm;
import ru.itis.tasktracker.model.Status;
import ru.itis.tasktracker.model.Task;
import ru.itis.tasktracker.services.TaskMapper;

import java.time.LocalDateTime;

public class TaskMapperImpl implements TaskMapper {
    @Override
    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .projectId(task.getProjectId())
                .build();
    }

    @Override
    public Task toTask(TaskForm taskForm) {
        Task task = Task.builder()
                .id(taskForm.getId())
                .name(taskForm.getName())
                .description(taskForm.getDescription())
                .deadline(LocalDateTime.parse(taskForm.getDeadline()))
                .status(Status.valueOf(taskForm.getStatus()))
                .projectId(taskForm.getProjectId())
                .build();
        if (taskForm.getCreatedAt() != null) task.setCreatedAt(LocalDateTime.parse(taskForm.getCreatedAt()));
        return task;
    }

    @Override
    public TaskDto toDto(TaskForm taskForm) {
        TaskDto taskDto = TaskDto.builder()
                .id(taskForm.getId())
                .name(taskForm.getName())
                .description(taskForm.getDescription())
                .deadline(LocalDateTime.parse(taskForm.getDeadline()))
                .status(Status.valueOf(taskForm.getStatus()))
                .build();
        if (taskForm.getCreatedAt() != null) taskDto.setCreatedAt(LocalDateTime.parse(taskForm.getCreatedAt()));
        return taskDto;
    }

    @Override
    public Task toTask(TaskDto task) {
        return Task.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .createdAt(task.getCreatedAt())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .projectId(task.getProjectId())
                .build();
    }
}
