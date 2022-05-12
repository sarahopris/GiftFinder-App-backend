package com.bachelorwork.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idItem;
    private String itemName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id_category")
    @JsonIgnore
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id_item"),
            inverseJoinColumns = @JoinColumn( name = "tags_id_tags"))
    private List<Tag> tagList = new ArrayList<>();


}
