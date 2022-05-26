package com.bachelorwork.backend.repository;

import com.bachelorwork.backend.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITagRepository extends CrudRepository<Tag, Long> {


    Optional<Tag> findByIdTag(Long id);
    Optional<Tag> findByTagName(String tagName);
    Optional<Tag> findByMandatory(Short mandatory);


}
