package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Optional<List<MessageEntity>> findAll(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return Optional.of(this.messageRepository.findAll(sort));
    }
}
