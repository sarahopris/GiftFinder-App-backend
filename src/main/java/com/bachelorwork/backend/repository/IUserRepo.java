package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepo extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
