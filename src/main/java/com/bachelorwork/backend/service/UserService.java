package com.bachelorwork.backend.service;

import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService {

    @Autowired
    private IUserRepo iUserRepo;

    public List<UserDTO> findAll() {
        return ((List<User>) iUserRepo.findAll()).stream()
                .map(this::convertToUserDTO).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        return iUserRepo.findById(id).stream().map(this::convertToUserDTO).findFirst().orElse(null);
    }


    @Transactional
    public boolean deleteUserById(Long id) {
        if (findById(id) == null)
            return false;
        iUserRepo.deleteById(id);
        return true;
    }

    public User convertToUser(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
//                .password(passwordEncoder.encode(userDTO.getPassword()))

               // .userNotificationsList(userDTO.getUserNotificationsList())
                .build();
    }


    public UserDTO convertToUserDTO(User user) {

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(user.getToken())
//                .password(user.getPassword())
                .selectedTags(user.getSelectedTags())
                .build();
//        userDTO.add(linkTo(UserController.class).slash(user.getIdUser()).withSelfRel());
//        userDTO.add(linkTo(methodOn(BugController.class).findBugsAssignedTo(userDTO.getUsername())).withRel("AsignedTo"));
//        userDTO.add(linkTo(methodOn(BugController.class).findBugsCreatedBy(userDTO.getUsername())).withRel("CreatedBy"));
        return userDTO;
    }

    public String generateUsername(String firstName, String lastName) {
        firstName = firstName.toLowerCase(Locale.ROOT);
        lastName = lastName.toLowerCase(Locale.ROOT);
        int i = 1;
        String prenume = "";
        String nume = "";
        if (lastName.length() >= 5) {
            prenume = Character.toString(firstName.charAt(0));
            nume = lastName.substring(0, 5);
        } else {
            prenume = firstName.substring(0, 3);
            nume = lastName;
        }

        String username = nume + prenume;
        while (iUserRepo.findByUsername(username) != null) {
            username = nume + prenume + Integer.toString(i);
            i++;
        }

        return username.toLowerCase();

    }


    public String addUser(UserDTO userDTO) {
        // username and email can not be null

        if ( userDTO.getUsername() == null || userDTO.getEmail() == null) {

            return "User not added to the database! You have to specify your first name, last name, mobile number and email to be able to add a new user.";
        }
        userDTO.setUsername(userDTO.getUsername());

        User user = convertToUser(userDTO);
        user.setPassword(userDTO.getPassword());

        if (user.isValidEmail()) {
            iUserRepo.save(user);
            return userDTO.getUsername() + " added successfully!";
        } else return "invalid email address";

    }
}
