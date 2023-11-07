package org.fanaticups.fanaticupsBack.security.dao;

import java.util.List;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", nullable = false)
private Long id;

@Column(name = "name")
private String name;

@Column(name = "email")
private String email;

@Column(name = "password")
private String password;

@Column(name = "roles")
private String roles;

@OneToMany(mappedBy = "user")
private List<CupEntity> cups;

// for test jpa.dialect
// @Column(name = "test")
// private String test;


}
