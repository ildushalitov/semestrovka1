package ru.itis.tasktracker.repositories;


import ru.itis.tasktracker.dto.MemberDto;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.repositories.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    void deleteMemberFromProject(Integer userId, Integer projectID);
    List<MemberDto> getMembersByProject(Integer id);

    Optional<User> findByEmail(String email);

    void addUserToProject(String email, Integer userId, Boolean isAdmin);
    Boolean isMember(Integer projectId, Integer userId);

    Boolean isAdmin(Integer projectId, Integer userId);
}
