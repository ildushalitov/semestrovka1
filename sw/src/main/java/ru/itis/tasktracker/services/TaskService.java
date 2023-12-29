package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.TaskDto;
import ru.itis.tasktracker.dto.TaskForm;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.model.Task;

import java.util.List;

public interface TaskService {
    TaskDto update(TaskForm form);
    TaskDto save(TaskForm form, Integer projectId);
    List<TaskDto> getTasks(Integer id);
    TaskDto getTask(String id);

    void delete(String id);
}
