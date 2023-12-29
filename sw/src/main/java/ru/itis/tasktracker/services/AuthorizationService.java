package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.SignInForm;
import ru.itis.tasktracker.dto.SignUpForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;

public interface AuthorizationService {
    UserDto signUp(SignUpForm form) throws TaskTrackerException;
    UserDto signIn(SignInForm form) throws TaskTrackerException;
}
