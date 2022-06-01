package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.Receiver;
import com.bachelorwork.backend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReceiverRepository extends CrudRepository<Receiver, Long> {
    Optional<Receiver> findByName(String name);
    List<Receiver> findByUser(User user);
}
