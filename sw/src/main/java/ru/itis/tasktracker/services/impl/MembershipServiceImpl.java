package ru.itis.tasktracker.services.impl;

import lombok.AllArgsConstructor;
import ru.itis.tasktracker.dto.MemberDto;
import ru.itis.tasktracker.dto.MemberForm;
import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.Project;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.UserRepository;
import ru.itis.tasktracker.repositories.impl.UserRepositoryJdbcImpl;
import ru.itis.tasktracker.services.MembershipService;
import ru.itis.tasktracker.services.ProjectService;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

public class MembershipServiceImpl implements MembershipService {
    UserRepository userRepository;
    ProjectService projectService;

    @Override
    public void addMember(MemberForm form) {
        if (form.getEmail().isEmpty() || form.getProjectId().isEmpty() || form.getIsAdmin().isEmpty()) {
            throw new TaskTrackerException("Cannot add a member. Parameters cannot be blank");
        }
        userRepository.addUserToProject(form.getEmail(), Integer.parseInt(form.getProjectId()), Boolean.parseBoolean(form.getIsAdmin()));
    }
    @Override
    public void deleteMember(String userId, String projectId){
        if (userId == null || projectId == null) throw new TaskTrackerException("No comment id provided");
        userRepository.deleteMemberFromProject(Integer.parseInt(userId), Integer.parseInt(projectId));
    }

    @Override
    public boolean isOwner(String projectId, String userId){
        Integer projectIdInt = Integer.parseInt(projectId);
        Integer userIdInt = Integer.parseInt(userId);
        ProjectDto project = projectService.getProject(projectIdInt);
        return Objects.equals(project.getOwnerId(), userIdInt);
    }
    @Override
    public List<MemberDto> getMembersByProject(String id) {
        Integer projectId = Integer.parseInt(id);
        return userRepository.getMembersByProject(projectId);
    }

    @Override
    public Boolean isMember(String projectId, String userId) {
        Integer projectIdInt = Integer.parseInt(projectId);
        Integer userIdInt = Integer.parseInt(userId);
        return userRepository.isMember(projectIdInt, userIdInt);
    }

    @Override
    public Boolean isAdmin(String projectId, String userId) {
        Integer projectIdInt = Integer.parseInt(projectId);
        Integer userIdInt = Integer.parseInt(userId);
        return userRepository.isAdmin(projectIdInt, userIdInt);
    }

}
