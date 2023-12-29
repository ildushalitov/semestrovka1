package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;
import ru.itis.tasktracker.dto.CommentDto;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerDBException;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Comment;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.CommentRepository;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.services.CommentMapper;
import ru.itis.tasktracker.services.CommentService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    UserRepository userRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    @Override
    public CommentDto save(String content, UserDto user, String taskId) {
        if (content == null || user == null || taskId == null) {
            throw new TaskTrackerException("Cannot save the comment");
        }
        Comment comment = Comment.builder().createdAt(LocalDateTime.now())
                .content(content)
                .userId(user.getId())
                .taskId(Integer.parseInt(taskId))
                .build();
        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByTask(String taskId) {
        if (taskId == null) throw new TaskTrackerDBException("This task does not exist");

        return commentRepository.findByTaskId(Integer.parseInt(taskId))
                .stream()
                .map(comment -> {
                    try {
                        CommentDto commentDto = commentMapper.toDto(comment);
                        Integer userId = comment.getUserId();
                        User user = userRepository.findById(userId).get();
                        commentDto.setEmail(user.getEmail());
                        return commentDto;
                    } catch (SQLException e) {
                        throw new TaskTrackerException(e.getMessage());
                    }


                })
                .collect(Collectors.toList());

    }
}
