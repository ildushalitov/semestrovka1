package ru.itis.tasktracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class CommentDto {
    Integer id;
    Integer userId;
    String content;
    Integer taskId;
    LocalDateTime createdAt;
    String email;
}
