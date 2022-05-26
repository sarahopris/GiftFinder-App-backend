package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findUserByUsername(String username);
    User findByUsername(String username);
}
