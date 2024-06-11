package org.fanaticups.fanaticupsBack.dao.repositories;


import org.fanaticups.fanaticupsBack.dao.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
