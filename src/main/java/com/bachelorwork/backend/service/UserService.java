package com.bachelorwork.backend.service;

import com.bachelorwork.backend.dto.UserDTO;
import com.bachelorwork.backend.model.User;
import com.bachelorwork.backend.repository.IUserRepository;
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

@Service
public class UserService {

    @Autowired
    private IUserRepository iUserRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<UserDTO> findAll() {
        return ((List<User>) iUserRepository.findAll()).stream()
                .map(this::convertToUserDTO).collect(Collectors.toList());
    }


    public UserDTO findById(Long id) {
        return iUserRepository.findById(id).stream().map(this::convertToUserDTO).findFirst().orElse(null);
    }


    public UserDTO findByUsername(String username) {
        return iUserRepository.findUserByUsername(username).stream().map(this::convertToUserDTO).findFirst().orElse(null);
    }


    @Transactional
    public boolean deleteUserById(Long id) {
        if (findById(id) == null)
            return false;
        iUserRepository.deleteById(id);
        return true;
    }

    public User convertToUser(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .receiversList(userDTO.getReceiversList())
                .build();
    }


    public UserDTO convertToUserDTO(User user) {

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .token(user.getToken())
                .receiversList(user.getReceiversList())
                .build();
        return userDTO;
    }


    @Transactional
    public ResponseEntity<?> addUser(UserDTO userDTO) {
        userDTO.setUsername(userDTO.getUsername());
        User user = convertToUser(userDTO);
        if (user.isValidPassword()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (user.isValidEmail()) {
            iUserRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public UserDTO updateUser(UserDTO userDto) {
        User user = iUserRepository.findByUsername(userDto.getUsername());
        if (user == null)
            return null;
        User userToSave = convertToUser(userDto);

        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userToSave.setIdUser(user.getIdUser());
        return convertToUserDTO(iUserRepository.save(userToSave));
    }

}
