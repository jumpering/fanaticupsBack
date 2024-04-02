package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.ChatRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
//    @Autowired
//    private MessageService messageService;
    @Autowired
    private CupService cupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public void add(CupDTO cupDTO){
        ChatEntity chatEntity = new ChatEntity();
        CupEntity cupEntity = this.modelMapper.map(cupDTO, CupEntity.class);
        chatEntity.setCup(cupEntity);
        this.chatRepository.save(chatEntity);
    }

    public Optional<List<MessageEntity>> findAllMessages(Long cupId){
        CupDTO cupDTO = this.cupService.findCupById(cupId).get();
        ChatEntity chatEntity = this.chatRepository.findByCup_Id(cupDTO.getId()).get();
        return Optional.of(chatEntity.getMessages());
        //return Optional.of(this.messageService.findAll(cupId, userId));
    }
}
