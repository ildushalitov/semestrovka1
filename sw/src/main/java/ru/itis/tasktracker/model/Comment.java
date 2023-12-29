package ru.itis.tasktracker.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Comment {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private Integer taskId;
    private Integer userId;
}
