package ru.itis.tasktracker.repositories.impl;

import lombok.RequiredArgsConstructor;
import ru.itis.tasktracker.exceptions.TaskTrackerDBException;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Comment;
import ru.itis.tasktracker.repositories.CommentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepositoryJdbcImpl implements CommentRepository {
    private final DataSource dataSource;

    private final String SQL_SELECT_BY_TASK_ID = "SELECT * FROM comments WHERE task_id = ?";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM comments WHERE id = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM comments";
    private final String SQL_DELETE = "DELETE cascade FROM comments WHERE id = ?";
    private final String SQL_INSERT = "INSERT INTO comments(content, created_at, task_id, user_id) VALUES (?, ?, ?, ?)";

    @Override
    public List<Comment> findByTaskId(Integer id) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_TASK_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(mapComment(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return comments;
    }

    @Override
    public Optional<Comment> findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapComment(resultSet));
            }
        } catch (SQLException e) {
            throw new TaskTrackerDBException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                comments.add(mapComment(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return comments;
    }

    @Override
    public Comment save(Comment item) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, item.getContent());
                statement.setTimestamp(2, Timestamp.valueOf(item.getCreatedAt()));
                statement.setInt(3, item.getTaskId());
                statement.setInt(4, item.getUserId());

                int affectedRows = statement.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException("Cannot insert comment");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getInt(1));
                        return item;
                    } else {
                        throw new SQLException("Cannot retrieve id");
                    }
                }
            }
        } catch (SQLException e) {
            throw new TaskTrackerDBException("Cannot add a comment");
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Comment with ID: " + id + " not found, deletion failed");
            }
        } catch (SQLException e) {
            throw new TaskTrackerDBException(e.getMessage());
        }
    }

    private Comment mapComment(ResultSet resultSet) throws SQLException {
        return Comment.builder()
                .id(resultSet.getInt("id"))
                .content(resultSet.getString("content"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .taskId(resultSet.getInt("task_id"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}
