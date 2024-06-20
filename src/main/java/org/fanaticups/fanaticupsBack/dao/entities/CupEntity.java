package org.fanaticups.fanaticupsBack.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;

import lombok.Data;

import java.util.List;


@Data
@Entity(name = "cups")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    
    @Column(name = "origin")
    private String origin;

    @Column(name = "image")
    private String image;
    
    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JsonBackReference //para que no haga recursión
    private UserEntity user;

    @ManyToMany()
    @JoinTable(
            name = "cups_categories",
            joinColumns = @JoinColumn(name = "cup_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> cupCategoriesList;
}
