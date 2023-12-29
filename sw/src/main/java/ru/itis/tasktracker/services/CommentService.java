package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.CommentDto;
import ru.itis.tasktracker.dto.UserDto;

import java.util.List;

public interface CommentService {
    CommentDto save(String content, UserDto user, String taskId);

    List<CommentDto> getCommentsByTask(String task);
}
