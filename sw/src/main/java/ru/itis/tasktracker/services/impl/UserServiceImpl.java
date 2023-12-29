package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.services.UserService;

import java.sql.SQLException;

@AllArgsConstructor

public class UserServiceImpl implements UserService {

    UserRepository userRepository;


    @Override
    public String getEmailById(String id) {
        Integer userId = Integer.parseInt(id);
        try {
            User user = userRepository.findById(userId).get();
            return user.getEmail();
        } catch (SQLException e) {
            throw new TaskTrackerException(e.getMessage());
        }

    }
}
