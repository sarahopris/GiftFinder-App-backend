package com.bachelorwork.backend.controller;

import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userTag")
@CrossOrigin("*")
public class UserTagController {

    @Autowired
    private UserTagService userTagService;

    @PostMapping("/addTagsLists")
    public ResponseEntity<?> addTagsToUser(@RequestParam String username,@RequestParam String[] tags){
        return userTagService.addTagToUser(username,tags);
    }

//    @GetMapping(value = "/getMandatoryTags", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Tag> getMandatoryTags(@RequestParam String username){
//        return userTagService.getMandatoryTags(username);
//    }

    @GetMapping(value = "/getOptionalTags", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tag> getOptionalTags(@RequestParam String username){
        return userTagService.getOptionalTags(username);
    }
}
