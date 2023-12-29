package ru.itis.tasktracker.repositories.impl;


import lombok.RequiredArgsConstructor;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Status;
import ru.itis.tasktracker.model.Task;
import ru.itis.tasktracker.repositories.TaskRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskRepositoryJdbcImpl implements TaskRepository {

    private final DataSource dataSource;

    private final String SQL_INSERT = "INSERT INTO tasks(name, description, created_at, deadline, status, project_id) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_BY_PROJECT_ID = "SELECT * FROM tasks WHERE project_id = ?";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM tasks WHERE id = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM tasks";
    private final String SQL_DELETE = "DELETE FROM tasks WHERE id = ?";
    private final String SQL_UPDATE = "UPDATE tasks SET name = ?, description = ?, created_at = ?, deadline = ?, status = ?, project_id = ? WHERE id = ?";
    //language = sql

    @Override
    public List<Task> findByProjectId(Integer id) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_PROJECT_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tasks.add(mapTask(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return tasks;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapTask(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()) {
                tasks.add(mapTask(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return tasks;
    }

    @Override
    public Task save(Task task) {
        try (Connection connection = dataSource.getConnection()) {
            if (task.getId() == null) {
                try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, task.getName());
                    statement.setString(2, task.getDescription());
                    statement.setTimestamp(3, Timestamp.valueOf(task.getCreatedAt()));
                    statement.setTimestamp(4, Timestamp.valueOf(task.getDeadline()));
                    statement.setString(5, task.getStatus().name());
                    statement.setInt(6, task.getProjectId());

                    int affectedRows = statement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException("Cannot insert task");
                    }

                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            task.setId(generatedKeys.getInt(1));
                            return task;
                        } else {
                            throw new SQLException("Cannot retrieve id");
                        }
                    }
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                    statement.setString(1, task.getName());
                    statement.setString(2, task.getDescription());
                    statement.setTimestamp(3, Timestamp.valueOf(task.getCreatedAt()));
                    statement.setTimestamp(4, Timestamp.valueOf(task.getDeadline()));
                    statement.setString(5, task.getStatus().name());
                    statement.setInt(6, task.getProjectId());
                    statement.setInt(7, task.getId());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated == 0) {
                        throw new SQLException("Task with ID: " + task.getId() + " not found, update failed");
                    }
                    return task;
                }
            }
        } catch (SQLException e) {
            throw new TaskTrackerException("Cannot apply the changes");
        }
    }


    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Task with ID: " + id + " not found, deletion failed");
            }
        } catch (SQLException e) {
            throw new TaskTrackerException("Cannot delete the project");
        }
    }




    private Task mapTask(ResultSet resultSet) throws SQLException {
        return Task.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .deadline(resultSet.getTimestamp("deadline").toLocalDateTime())
                .status(Status.valueOf(resultSet.getString("status")))
                .projectId(resultSet.getInt("project_id"))
                .build();
    }
}