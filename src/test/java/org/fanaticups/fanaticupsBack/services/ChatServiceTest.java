package org.fanaticups.fanaticupsBack.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    private ChatService chatService;

    @BeforeEach
    public void setUp(){

    }

    @Test
    public void givenCupIdWhenDeleteChatThenReturnNothing(){
        // arrange
        Long cupId = 1L;
        // act
        this.chatService.deleteCupChat(1L);
        // assert
        Mockito.verify(this.chatService, Mockito.times(1)).deleteCupChat(anyLong());
    }
}
