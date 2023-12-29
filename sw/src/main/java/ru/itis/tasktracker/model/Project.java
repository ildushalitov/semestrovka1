package ru.itis.tasktracker.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Project {
    private Integer id;
    private String name;
    private String description;
    private Integer ownerId;
    private LocalDateTime createdAt;
}
