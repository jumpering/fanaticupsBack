package org.fanaticups.fanaticupsBack.security.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Integer>{
  
    public Optional<UserEntity> findById(Long id);

    public Optional<UserEntity> findByName(String name);

    public Optional<UserEntity> findOneByEmail(String email);
}
