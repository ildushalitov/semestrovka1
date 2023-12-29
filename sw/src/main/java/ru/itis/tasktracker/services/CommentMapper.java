package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.CommentDto;
import ru.itis.tasktracker.model.Comment;

public interface CommentMapper {
    CommentDto toDto(Comment comment);
}
