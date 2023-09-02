package org.fanaticups.fanaticupsBack.dao.repositories;

import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CupRepository extends JpaRepository<CupEntity, Long>{

    public List<CupEntity> findAll();

    public Optional<CupEntity> findById(Long id);
    
}
