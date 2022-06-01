package com.bachelorwork.backend.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;
    @NotNull
    private String email;
    @Column(unique = true)
    private String username;

    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_tag",
                joinColumns = @JoinColumn(name = "users_id_user"),
                inverseJoinColumns = @JoinColumn( name = "tags_id_tags"))
    private List<Tag> optionalTags = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_tag",
            joinColumns = @JoinColumn(name = "users_id_user"),
            inverseJoinColumns = @JoinColumn( name = "tags_id_tags"))
    private List<Tag> mandatoryTags = new ArrayList<>();
    private String token;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Receiver> receiversList;


    public boolean isValidEmail() {
        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean isValidPassword()
    {   String passwordValidationRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>_]).{8,20}$";
        Pattern pattern = Pattern.compile(passwordValidationRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

//    public boolean isValidUsername(){
//
//    }


    public boolean isValid() {
        return username != null && email!= null && isValidEmail() && isValidPassword();
    }

}
