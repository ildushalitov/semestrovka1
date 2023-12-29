package ru.itis.tasktracker.dto;

import ru.itis.tasktracker.model.Status;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TaskForm {
    private Integer id;
    private String name;
    private String description;
    private String deadline;
    private String status;
    private String createdAt;
    private Integer projectId;
}
