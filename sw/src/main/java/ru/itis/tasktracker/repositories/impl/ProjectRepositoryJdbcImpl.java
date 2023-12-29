package ru.itis.tasktracker.repositories.impl;

import lombok.RequiredArgsConstructor;
import ru.itis.tasktracker.exceptions.TaskTrackerDBException;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.model.Task;
import ru.itis.tasktracker.repositories.ProjectRepository;
import ru.itis.tasktracker.repositories.TaskRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProjectRepositoryJdbcImpl implements ProjectRepository {

    private final DataSource dataSource;


    private final String SQL_INSERT = "INSERT INTO projects(name, description, created_at, owner_id) VALUES (?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM projects WHERE id = ?";
    private final String SQL_SELECT_ALL = "SELECT * FROM projects";
    private final String SQL_DELETE = "DELETE FROM projects WHERE id = ?";
    private final String SQL_UPDATE = "UPDATE projects SET name = ?, description = ?, created_at = ?, owner_id = ? WHERE id = ?";
    //language = sql
    private final String SQL_SELECT_BY_USER_ID = "SELECT project_id FROM project_membership WHERE user_id = ?";
    private final String SQL_ADD_PROJECT_MEMBERSHIP = "INSERT INTO project_membership(user_id, project_id, is_admin) values (?,?,?)";

    @Override
    public Optional<Project> findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapProject(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findByUserId(Integer id) throws TaskTrackerException {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_USER_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                projects.add(findById(resultSet.getInt("project_id")).get());

            }
        } catch (SQLException e) {
            throw new TaskTrackerDBException("Cannot do the operation");
        }
        return projects;
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()) {
                projects.add(mapProject(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return projects;
    }

    @Override
    public Project save(Project project) {
        try (Connection connection = dataSource.getConnection()) {
            if (project.getId() == null) {
                try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, project.getName());
                    statement.setString(2, project.getDescription());
                    statement.setTimestamp(3, Timestamp.valueOf(project.getCreatedAt()));
                    statement.setInt(4, project.getOwnerId());
                    int affectedRows = statement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException("Cannot insert project");
                    }

                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {


                            project.setId(generatedKeys.getInt(1));
                            try (PreparedStatement membershipStatement = connection.prepareStatement(SQL_ADD_PROJECT_MEMBERSHIP)) {

                                membershipStatement.setInt(1, project.getOwnerId());
                                membershipStatement.setInt(2, project.getId());
                                membershipStatement.setBoolean(3, true);

                                int membershipRowsAffected = membershipStatement.executeUpdate();

                                if (membershipRowsAffected != 1) {
                                    throw new SQLException("Cannot add user to project membership");
                                }
                            }
                            return project;
                        } else {
                            throw new SQLException("Cannot retrieve id");
                        }
                    }
                }

            } else {
                try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                    statement.setString(1, project.getName());
                    statement.setString(2, project.getDescription());
                    statement.setTimestamp(3, Timestamp.valueOf(project.getCreatedAt()));
                    statement.setInt(4, project.getOwnerId());
                    statement.setInt(5, project.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated == 0) {
                        throw new SQLException("Project with ID: " + project.getId() + " not found, update failed");
                    }
                    return project;
                }
            }
        } catch (SQLException e) {
            throw new TaskTrackerException("Incorrect input. Length of name cannot be more than 30");
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Project with ID: " + id + " not found, deletion failed");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Project mapProject(ResultSet resultSet) throws SQLException {
        return Project.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .ownerId(resultSet.getInt("owner_id"))
                .build();
    }
}
