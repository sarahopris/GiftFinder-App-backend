package com.bachelorwork.backend.security;

import com.bachelorwork.backend.dto.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class LoginController {
    @Autowired
    UserSecurityService userSecurityService;

    @PostMapping("user/login")
    public ResponseEntity<?> login(@RequestBody LoginData loginData) throws Exception {
        return userSecurityService.login(loginData);
    }

    @PostMapping("user/logout")
    public ResponseEntity<?> logout(@RequestBody String username) throws Exception{
        return userSecurityService.logout(username);
    }
}

