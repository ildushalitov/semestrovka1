package ru.itis.tasktracker.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ProjectDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private Integer ownerId;
}
