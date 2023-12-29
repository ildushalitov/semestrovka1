package ru.itis.tasktracker.repositories.impl;

import ru.itis.tasktracker.dto.MemberDto;
import ru.itis.tasktracker.exceptions.TaskTrackerDBException;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    //language=sql
    private final String SQL_INSERT = "insert into users(first_name,last_name,email,password_hash) values (?, ?, ?,?)";
    //language=sql
    private final String SQL_SELECT_BY_ID = "select * from users where id = ?";

    //language=sql
    private final String SQL_SELECT_ALL = "select * from users";
    private final String SQL_DELETE = "delete from users where id = ?";
    //language=sql
    private final String SQL_UPDATE = "update users set first_name = ?, last_name = ?, email = ?, password_hash = ? where id = ?";
    //language=sql
    private final String SQL_FIND_BY_EMAIL = "select * from users where email = ?";
    private final String SQL_ADD_USER_TO_PROJECT = "INSERT INTO project_membership(user_id, project_id, is_admin) values(?,?,?)";
    private final String SQL_IS_MEMBER = "select exists(select * from project_membership where user_id = ? and project_id = ?)";
    private final String SQL_IS_ADMIN = "select is_admin from project_membership where user_id = ? and project_id = ?";
    //language=sql
    private final String SQL_GET_USERS_BY_PROJECT = "select user_id from project_membership where project_id=?";
    private final String SQL_DELETE_MEMBERS = "delete from project_membership where user_id = ? and project_id = ?";


    private final DataSource dataSource;


    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void deleteMemberFromProject(Integer userId, Integer projectID){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_MEMBERS)) {
            statement.setInt(1,userId);
            statement.setInt(2,projectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskTrackerDBException("Cannot delete member");
        }
    }
    @Override
    public List<MemberDto> getMembersByProject(Integer id) {
        List<MemberDto> members = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_USERS_BY_PROJECT)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer userId = resultSet.getInt("user_id");
                Optional<User> userOptional = findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    Boolean isAdmin = isAdmin(id, userId);
                    MemberDto memberDto = MemberDto.builder()
                            .id(userId)
                            .email(user.getEmail())
                            .projectId(id)
                            .isAdmin(isAdmin)
                            .build();
                    members.add(memberDto);
                }
            }
        } catch (SQLException e) {
            throw new TaskTrackerDBException("Error with project: " + id + " :(");
        }

        return members;
    }

    @Override
    public Optional<User> findById(Integer id) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println(resultSet.getString("first_name"));
            User user = User.builder().id(resultSet
                            .getInt("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .passwordHash(resultSet.getString("password_hash"))
                    .email(resultSet.getString("email")).build();
            System.out.println("норм3");
            return Optional.ofNullable(user);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
        while (resultSet.next()) {
            User user = User.builder().id(resultSet
                            .getInt("id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .passwordHash(resultSet.getString("password_hash"))
                    .email(resultSet.getString("email")).build();
            users.add(user);
        }
        return users;
    }

    @Override
    public User save(User model) {
        try (Connection connection = dataSource.getConnection()) {
            if (model.getId() == null) {
                try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, model.getFirstName());
                    statement.setString(2, model.getLastName());
                    statement.setString(3, model.getEmail());
                    statement.setString(4, model.getPasswordHash());
                    int affectedRows = statement.executeUpdate();

                    if (affectedRows != 1) {
                        throw new SQLException("Cannot insert user");
                    }

                    try (ResultSet generatedIds = statement.getGeneratedKeys()) {
                        if (generatedIds.next()) {
                            model.setId(generatedIds.getInt("id"));
                            return model;
                        } else {
                            throw new SQLException("Cannot retrieve id");
                        }
                    }
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                    statement.setString(1, model.getFirstName());
                    statement.setString(2, model.getLastName());
                    statement.setString(3, model.getEmail());
                    statement.setString(4, model.getPasswordHash());
                    statement.setInt(5, model.getId());

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated == 0) {
                        throw new SQLException("User with ID: " + model.getId() + " not found, update failed");
                    }
                    return model;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    ;

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
                statement.setInt(1, id);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted == 0) {
                    throw new SQLException("User with ID: " + id + " not found, deletion failed");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_EMAIL)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    User user = User.builder()
                            .id(resultSet.getInt("id"))
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .passwordHash(resultSet.getString("password_hash"))
                            .email(resultSet.getString("email"))
                            .build();
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void addUserToProject(String email, Integer projectId, Boolean isAdmin) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ADD_USER_TO_PROJECT)
        ) {

            Optional<User> user = findByEmail(email);
            if (user.isEmpty()) {
                throw new TaskTrackerException("User with such email does not exist");
            }
            if (isMember(projectId, user.get().getId()))
                throw new TaskTrackerException("This user is already a member of this project");
            statement.setInt(1, user.get().getId());
            statement.setInt(2, projectId);
            statement.setBoolean(3, isAdmin);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new TaskTrackerException("Cannot add a member");
        }
    }

    @Override
    public Boolean isMember(Integer projectId, Integer userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_IS_MEMBER)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, projectId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("exists").equals("t");
            } else {
                throw new TaskTrackerDBException("Something went wrong");
            }

        } catch (SQLException e) {
            throw new TaskTrackerDBException("Something went wrong :(");
        }
    }

    @Override
    public Boolean isAdmin(Integer projectId, Integer userId) {
        if (!isMember(projectId, userId)) return false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_IS_ADMIN)
        ) {
            statement.setInt(1, userId);
            statement.setInt(2, projectId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("is_admin").equals("t");
            } else {
                throw new TaskTrackerDBException("Something went wrong");
            }

        } catch (SQLException e) {
            throw new TaskTrackerDBException("Something went wrong :(");
        }
    }
}
