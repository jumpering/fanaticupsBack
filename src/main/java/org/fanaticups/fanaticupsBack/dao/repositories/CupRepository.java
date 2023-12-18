package org.fanaticups.fanaticupsBack.dao.repositories;

import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CupRepository extends JpaRepository<CupEntity, Long>{

    public List<CupEntity> findAll();

    public Optional<CupEntity> findById(Long id);

    public boolean existsByName(String name);

    public boolean existsByUser_IdAndName(Long userId, String name);

//    @Query("select c from cups c where c.user.id = ?1")
//    public List<CupEntity> findAllByUserId(Long id);


    
}
