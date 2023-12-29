package ru.itis.tasktracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class MemberForm {
    String projectId;
    String email;
    String isAdmin;
}
