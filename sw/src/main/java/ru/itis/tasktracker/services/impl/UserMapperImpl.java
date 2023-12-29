package ru.itis.tasktracker.services.impl;

import ru.itis.tasktracker.dto.SignUpForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.services.UserMapper;

public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fistName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public User toUser(UserDto dto) {
        return User.builder()
                .firstName(dto.getFistName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();
    }

    @Override
    public User toUser(SignUpForm dto) {
        return User.builder()
                .firstName(dto.getFistName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .passwordHash(dto.getPassword())
                .build();
    }

}
