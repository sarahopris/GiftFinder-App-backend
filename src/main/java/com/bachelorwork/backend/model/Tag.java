package com.bachelorwork.backend.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tag", schema = "public")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTag;

    @Column(unique = true)
    private String tagName;
    private Short mandatory;
}
