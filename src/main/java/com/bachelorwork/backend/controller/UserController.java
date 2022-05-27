package com.bachelorwork.backend.controller;


import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.IUserRepository;
import com.bachelorwork.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository iUserRepository;


    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDTO> findAll() {
        return userService.findAll();
    }


    @GetMapping(value = "/getById/id={id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }


    @GetMapping(value = "/getByUsername/username={username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO findByUsername(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }


    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDto) {
        return userService.addUser(userDto);
    }


    @PutMapping("/changeUser")
    public UserDTO changeUser(@RequestBody UserDTO userDto) {
        return userService.updateUser(userDto);
    }

}
