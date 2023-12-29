package ru.itis.tasktracker.services;

import ru.itis.tasktracker.dto.MemberDto;
import ru.itis.tasktracker.dto.MemberForm;

import java.util.List;

public interface MembershipService {
    boolean isOwner(String projectId, String userId);
    void addMember(MemberForm form);
    void deleteMember(String userId, String projectId);
    List<MemberDto> getMembersByProject(String id);
    Boolean isAdmin(String projectId, String userId);
    Boolean isMember(String projectId, String userId);
}
