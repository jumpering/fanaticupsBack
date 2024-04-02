package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.ChatRepository;
import org.fanaticups.fanaticupsBack.dao.repositories.MessageRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public void createCupChat(CupDTO cupDTO){
        ChatEntity chatEntity = new ChatEntity();
        CupEntity cupEntity = this.modelMapper.map(cupDTO, CupEntity.class);
        chatEntity.setCup(cupEntity);
        this.chatRepository.save(chatEntity);
    }

    public List<MessageEntity> findAllMessages(Long cupId){
        Optional<ChatEntity> chatEntity = this.chatRepository.findByCup_Id(cupId);
        List<MessageEntity> resultListOfMessages = new ArrayList<>();
        if(chatEntity.isPresent()){
            resultListOfMessages = chatEntity.get().getMessages();
        }
        return resultListOfMessages;
    }

    public Optional<MessageEntity> createMessage(Long cupId, Long userId, String message){
        Optional<ChatEntity> chat = this.chatRepository.findByCup_Id(cupId);
        Optional<UserEntity> userEntity = this.userRepository.findById(userId.intValue());
        if(userEntity.isPresent() && chat.isPresent()){
            MessageEntity messageToSend = MessageEntity.builder()
                    .chat(chat.get())
                    .message(message)
                    .localDate(LocalDate.now())
                    .user(userEntity.get())
                    .build();
            return Optional.of(this.messageRepository.save(messageToSend));
        }
        return Optional.empty();
    }
}
