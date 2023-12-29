package ru.itis.tasktracker.services.impl;

import ru.itis.tasktracker.dto.CommentDto;
import ru.itis.tasktracker.model.Comment;
import ru.itis.tasktracker.services.CommentMapper;

public class CommentMapperImpl implements CommentMapper {
    public CommentDto toDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTaskId())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUserId())
                .build();
    }
}
