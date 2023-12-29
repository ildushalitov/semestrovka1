package ru.itis.tasktracker.dto;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserDto {
    private Integer id;
    private String fistName;
    private String lastName;
    private String email;
}
