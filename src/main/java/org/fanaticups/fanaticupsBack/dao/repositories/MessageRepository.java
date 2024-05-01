package org.fanaticups.fanaticupsBack.dao.repositories;

import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    long deleteByChat(ChatEntity chat);
}
