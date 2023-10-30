package org.fanaticups.fanaticupsBack.dao.repositories;

import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Long>{
  
    public Optional<UserEntity> findById(Long id);

    public Optional<UserEntity> findByName(String name);

    public Optional<UserEntity> findByEmail(String email);

    //public boolean existByEmail(String email);

}
