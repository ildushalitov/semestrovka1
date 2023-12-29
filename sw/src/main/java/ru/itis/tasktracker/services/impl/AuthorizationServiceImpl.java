package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;
import ru.itis.tasktracker.dto.SignInForm;
import ru.itis.tasktracker.dto.SignUpForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.services.AuthorizationService;
import ru.itis.tasktracker.services.PasswordEncoder;
import ru.itis.tasktracker.services.UserMapper;

import java.util.Optional;

@AllArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto signUp(SignUpForm form) throws TaskTrackerException {
        if (form.getEmail() == null) {
            throw new TaskTrackerException("Email cannot be null");
        }
        Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());
        if (optionalUser.isPresent()) {
            throw new TaskTrackerException("User with email " + form.getEmail() + " already exist");
        }
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        User user = userMapper.toUser(form);
        User savedUser = userRepository.save(user);
        user.setId(userRepository.findByEmail(user.getEmail()).get().getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto signIn(SignInForm form) throws TaskTrackerException {
        if(form.getEmail() == null) {
            throw new TaskTrackerException("Email cannot be null");
        }
        Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());
        if(optionalUser.isEmpty()) {
            throw new TaskTrackerException("User with email " + form.getEmail() + " not found.");
        }
        User user = optionalUser.get();
        user.setId(userRepository.findByEmail(user.getEmail()).get().getId());
        if(!passwordEncoder.matches(form.getPassword(), user.getPasswordHash())) {
            throw new TaskTrackerException("Wrong password");
        }
        return userMapper.toDto(user);
    }
}
