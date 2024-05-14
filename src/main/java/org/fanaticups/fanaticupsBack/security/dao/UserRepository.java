package org.fanaticups.fanaticupsBack.security.dao;

import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<UserEntity, Long>{
  
//    public Optional<UserEntity> findById(Long id);
//
//    public Optional<UserEntity> findByName(String name);

    public Optional<UserEntity> findOneByEmail(String email);

    public boolean existsByEmail(String email);




    @Query("SELECT u.favoriteCupList FROM users u WHERE u.id = :userId")
    Page<CupEntity> findFavoriteProductsByUserId(Long userId, Pageable pageable);

//    @Query("SELECT u.favoriteCupList FROM users u WHERE u.id = :userId")
//    List<CupEntity> findFavoriteProductsByUserId(Long userId);



}
