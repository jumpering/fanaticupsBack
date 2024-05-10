package org.fanaticups.fanaticupsBack.security.dao;

import java.awt.print.Pageable;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long>{
  
//    public Optional<UserEntity> findById(Long id);
//
//    public Optional<UserEntity> findByName(String name);

    public Optional<UserEntity> findOneByEmail(String email);

    public boolean existsByEmail(String email);


}
