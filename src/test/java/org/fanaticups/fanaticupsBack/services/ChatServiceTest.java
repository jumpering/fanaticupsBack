package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.ChatEntity;
import org.fanaticups.fanaticupsBack.dao.entities.MessageEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.ChatRepository;
import org.fanaticups.fanaticupsBack.dao.repositories.MessageRepository;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    private Long userId;
    private Long cupId;
    private ChatEntity chatEntity;
    private UserEntity userEntity;
    private String message;
    private MessageEntity messageEntity;
    private LocalDate localDate = LocalDate.now();

    @BeforeEach
    public void setUp(){
        this.chatEntity = ChatEntity.builder()
                .id(1L)
                .build();
        this.userEntity = UserEntity.builder()
                .id(1L)
                .name("xavi")
                .email("a@a.a")
                .build();
        this.message = "testMessage";
        this.messageEntity = MessageEntity.builder()
                .message(this.message)
                .user(this.userEntity)
                .chat(this.chatEntity)
                .localDate(LocalDate.now())
                .build();
    }

    @Test
    public void givenNewMessageWhenCreateMessageThenReturnOptionalMessage(){
        //arrange
        Mockito.when(this.chatRepository.findByCup_Id(1L)).thenReturn(Optional.ofNullable(this.chatEntity));
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.ofNullable(this.userEntity));
        Mockito.when(this.messageRepository.save(this.messageEntity)).thenReturn(this.messageEntity);
        Optional<MessageEntity> expected = Optional.ofNullable(this.messageEntity);
        //act

        //assert
        Assertions.assertEquals(expected, this.chatService.createMessage(1L, 1L,this.message));
        Assertions.assertEquals(this.localDate,this.messageEntity.getLocalDate());

    }

    @Test
    public void givenCupIdWhenDeleteCupThenDeleteChatAndMessagesAndReturnNothing(){
        // arrange
        Long cupId = 1L;
        Mockito.when(this.messageRepository.deleteByChat(this.chatEntity)).thenReturn(1L);
        //Mockito.when(this.chatRepository.delete(this.chatEntity)).thenReturn(void);
        // act
        this.chatService.deleteCupChat(1L);
        // assert
        Mockito.verify(this.chatService, Mockito.times(1)).deleteCupChat(anyLong());
    }
}
