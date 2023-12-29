package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.SignUpForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.model.User;

public interface UserMapper {
    UserDto toDto(User user);
    User toUser(UserDto dto);
    User toUser(SignUpForm dto);
}
