package org.fanaticups.fanaticupsBack.security.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Integer>{
  
    public Optional<UserEntity> findById(Long id);
}
