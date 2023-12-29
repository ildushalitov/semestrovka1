package ru.itis.tasktracker.repositories.generic;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {
    Optional<T> findById(K id) throws java.sql.SQLException;
    List<T> findAll() throws java.sql.SQLException;
    T save(T item);
    void delete(K id);
}