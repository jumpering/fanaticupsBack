package org.fanaticups.fanaticupsBack.dao.repositories;


import org.fanaticups.fanaticupsBack.dao.entities.CategoryEntity;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c.cups FROM categories c WHERE c.id = :id")
    Page<CupEntity> findCupsByCategoryId(@Param("id") Long id, Pageable pageable);

}
