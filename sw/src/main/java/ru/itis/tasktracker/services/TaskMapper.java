package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.TaskForm;
import ru.itis.tasktracker.model.Task;

public interface TaskMapper {
    TaskDto toDto(Task project);
    Task toTask(TaskDto dto);
    Task toTask(TaskForm task);

    TaskDto toDto(TaskForm form);
}
