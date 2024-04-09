package org.fanaticups.fanaticupsBack.dao.repositories;

import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query("select c from ChatEntity c where c.cup.id = ?1")
    Optional<ChatEntity> findByCup_Id(Long id);

//    @Query("SELECT c FROM ChatEntity c JOIN FETCH c.messages m ORDER BY m.id")
//    Optional<ChatEntity> findByCup_Id(Long id);

//    Optional<ChatEntity> findFirstByCup_IdOrderByMessages_IdDesc(Long id);




}
