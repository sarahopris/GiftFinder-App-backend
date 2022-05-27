package com.bachelorwork.backend.service;

import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.repository.IUserRepository;
import com.bachelorwork.backend.repository.IUserTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserTagService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private ITagRepository iTagRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;


//    @Transactional
//    public void insertIntoUserTag(Long userId, Long tagId) {
//        iTagRepository.entityManager.createNativeQuery("INSERT INTO user_tag (users_id_user, tags_id_tags) VALUES (?,?)")
//                .setParameter(1, userId)
//                .setParameter(2, tagId)
//                .executeUpdate();
//    }

    public ResponseEntity<?> addTagToUser(String username, String[] tagNames) {
        Optional<User> user = iUserRepository.findUserByUsername(username);
        //Tag tag  = tagService.findByTagName(tagName);

        if (user.isPresent()) {
            Set<Tag> mandatoryTagList = new HashSet<>(user.get().getMandatoryTags()).stream().filter(tag -> tag.getMandatory() == 1).collect(Collectors.toSet());
            Set<Tag> optionalTagList = new HashSet<>(user.get().getOptionalTags()).stream().filter(tag -> tag.getMandatory() == 0).collect(Collectors.toSet());

            for (String tag : tagNames) {
                Tag tagAux = tagService.findByTagName(tag);
                if (tagAux != null) {
                    if (tagAux.getMandatory() == 1)
                        mandatoryTagList.add(tagAux);
                    else if (tagAux.getMandatory() == 0) {
                        optionalTagList.add(tagAux);
                    }
                }
            }
            List<Tag> mandatoryTags = new ArrayList<>(mandatoryTagList);
            List<Tag> optionalTags = new ArrayList<>(optionalTagList);
            user.get().setMandatoryTags(mandatoryTags);
            user.get().setOptionalTags(optionalTags);
            iUserRepository.save(user.get());
            return new ResponseEntity<>("tags added", HttpStatus.OK);
        } else
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }

    public List<Tag> getMandatoryTags(String username) {
        Optional<User> user = iUserRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return user.get().getMandatoryTags().stream().filter(tag -> tag.getMandatory() == 1).collect(Collectors.toList());
    }

    public List<Tag> getOptionalTags(String username) {
        Optional<User> user = iUserRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        return user.get().getOptionalTags().stream().filter(tag -> tag.getMandatory() == 0).collect(Collectors.toList());
    }
}
