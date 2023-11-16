package org.fanaticups.fanaticupsBack.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity(name = "cups")
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

//    @Column(name = "owner")
//    private String owner;

    @ManyToOne
    @JsonBackReference //para que no haga recursión
    private UserEntity user;
}
