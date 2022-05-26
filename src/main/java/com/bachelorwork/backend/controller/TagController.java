package com.bachelorwork.backend.controller;
import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.Tag;
import com.bachelorwork.backend.repository.ITagRepository;
import com.bachelorwork.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@CrossOrigin("*")
public class TagController {

    @Autowired
    TagService tagService;

    @Autowired
    ITagRepository iTagRepository;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @GetMapping(value = "/getById/id={id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tag findById(@PathVariable("id") Long id) {
        return tagService.findById(id);
    }

    @GetMapping(value = "/getByTagName/tagName={tagName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tag findById(@PathVariable("tagName") String tagName) {
        Tag tag = tagService.findByTagName(tagName);
        return tag;
    }

    @PostMapping("/addTag")
    public ResponseEntity<?> addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

}
