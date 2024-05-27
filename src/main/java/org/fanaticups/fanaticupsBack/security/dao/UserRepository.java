package org.fanaticups.fanaticupsBack.security.dao;

import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<UserEntity, Long>{

    public Optional<UserEntity> findOneByEmail(String email);

    public boolean existsByEmail(String email);

    @Query("SELECT u.favoriteCupList FROM users u WHERE u.id = :userId")
    Page<CupEntity> findFavoriteCupsByUserId(Long userId, Pageable pageable);

//    @Query("SELECT u.favoriteCupList FROM users u WHERE u.id = :userId")
//    List<CupEntity> findFavoriteCupListByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM fanaticups.favorites WHERE cup_id = ?1", nativeQuery = true)
    void deleteCupFromFavorites(Long cupId);
}
