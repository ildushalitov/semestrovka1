package ru.itis.tasktracker.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.time.Instant;
@Data
@Builder
public class Task {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Status status;
    private Integer projectId;
}
