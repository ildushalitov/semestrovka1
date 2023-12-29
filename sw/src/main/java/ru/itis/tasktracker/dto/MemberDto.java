package ru.itis.tasktracker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    Integer id;
    String email;
    Integer projectId;
    Boolean isAdmin;
}
