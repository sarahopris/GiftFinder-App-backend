package com.bachelorwork.backend.service;

import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    private ITagRepository iTagRepository;

    public List<Tag> findAll() {
        return new ArrayList<>(((List<Tag>) iTagRepository.findAll()));
    }

    public Tag findById(Long id) {
        return iTagRepository.findByIdTag(id).stream().findFirst().orElse(null);
    }

    public Tag findByTagName(String tagName){
        return iTagRepository.findByTagName(tagName).stream().findFirst().orElse(null);
    }


    @Transactional
    public ResponseEntity<?> addTag(Tag tag) {
//         username and email can not be null
//        if ( userDTO.getUsername() == null || userDTO.getEmail() == null) {
//            return "User not added to the database! You have to specify your username and email to be able to add a new user.";
//        }
        tag.setTagName(tag.getTagName());

        if(tag.getMandatory() != 1) {
            tag.setMandatory((short) 0);
        }
        else tag.setMandatory(tag.getMandatory());

        iTagRepository.save(tag);
        return new ResponseEntity<>("added tag", HttpStatus.OK);
    }

}

