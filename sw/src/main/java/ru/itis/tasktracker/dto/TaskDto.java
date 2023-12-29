package ru.itis.tasktracker.dto;

import ru.itis.tasktracker.model.Status;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class TaskDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Status status;
    private Integer projectId;
}
