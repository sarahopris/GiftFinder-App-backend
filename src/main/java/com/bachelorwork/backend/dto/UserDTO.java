package com.bachelorwork.backend.dto;

import com.bachelorwork.backend.model.Receiver;
import com.bachelorwork.backend.model.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {

    private String email;
    private String username;
    private String password;
    private String token;
    private List<Receiver> receiversList;


    public boolean isValid() {
        return this.isValidEmail() && this.usernameNotNull() ;
    }

    public boolean isValidEmail() {
        if (this.email == null) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@+[a-z]+.com";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean usernameNotNull() {
        return this.username != null;
    }




}
