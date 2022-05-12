package com.bachelorwork.backend.service;

import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .selectedTags(userDTO.getSelectedTags())
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

    @Transactional
    public ResponseEntity<?> addUser(UserDTO userDTO) {
//         username and email can not be null
//        if ( userDTO.getUsername() == null || userDTO.getEmail() == null) {
//            return "User not added to the database! You have to specify your username and email to be able to add a new user.";
//        }
        userDTO.setUsername(userDTO.getUsername());
        User user = convertToUser(userDTO);
        if(user.isValidPassword()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (user.isValidEmail()) {
            iUserRepo.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public UserDTO updateUser(UserDTO userDto) {
        User user = iUserRepo.findByUsername(userDto.getUsername());
        if (user == null)
            return null;
        User userToSave = convertToUser(userDto);

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setIdUser(user.getIdUser());
        return convertToUserDTO(iUserRepo.save(userToSave));
    }

}
