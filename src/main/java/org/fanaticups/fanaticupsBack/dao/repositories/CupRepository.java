package org.fanaticups.fanaticupsBack.dao.repositories;

import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;


public interface CupRepository extends JpaRepository<CupEntity, Long>{
//    @Transactional
//    default CupEntity update(CupEntity cupEntity){ //TODO ...use save
//        CupEntity actualCup = this.findById(cupEntity.getId()).orElse(null);
//        if(actualCup != null){
//            actualCup.setName(cupEntity.getName());
//        }
//        return null;
//    }
    //public List<CupEntity> findAll();

    public Page<CupEntity> findAll(Pageable pageable);

    public Page<CupEntity> findAllByNameContaining(Pageable pageable, String searchName);

    public Optional<CupEntity> findById(Long id);

    public void deleteById(Long id);

    public boolean existsByUser_IdAndName(Long userId, String name);

//    @Query("select c from cups c where c.user.id = ?1")
//    public List<CupEntity> findAllByUserId(Long id);


    
}
