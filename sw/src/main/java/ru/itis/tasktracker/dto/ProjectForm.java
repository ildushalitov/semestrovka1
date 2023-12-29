package ru.itis.tasktracker.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ProjectForm {
    private Integer id;
    private String name;
    private String description;
}
