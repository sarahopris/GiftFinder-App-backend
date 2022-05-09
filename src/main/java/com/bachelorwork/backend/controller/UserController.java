package com.bachelorwork.backend.controller;


import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/getById/id={id}")
    public UserDTO findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserDTO userDto) {
        return userService.addUser(userDto);
    }


}
