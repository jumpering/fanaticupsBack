package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void givenUserIdAndPageValuesWhenGetFavoriteCupListThenReturnPageCupDTO() {
        //arrange
        UserEntity userEntity = new UserEntity();
        userEntity .setId(1L);
        userEntity.setName("user");

        CupDTO cupDTO = CupDTO.builder()
                .id(1L)
                .name("test")
                .origin("CAT")
                .user(userEntity)
                .image("http://5.250.184.31:9000/images/fanaticups/1/160/imageTest.jpg")
                .build();

        CupEntity cupEntity = CupEntity.builder()
                .id(1L)
                .name("test")
                .origin("CAT")
                .user(userEntity)
                .image("test.jpg")
                .build();

        List<CupDTO> expectedCupDTOList = new ArrayList<>();
        expectedCupDTOList.add(cupDTO);
        List<CupEntity> expectedCupEntityList = new ArrayList<>();
        expectedCupEntityList.add(cupEntity);
        PageRequest pageable = PageRequest.of(0, 12);

        //act
        Page<CupEntity> expectedPageCupEntity = new PageImpl<>(expectedCupEntityList, pageable, expectedCupEntityList.size());
        Page<CupDTO> expectedPageCupDTO = new PageImpl<>(expectedCupDTOList, pageable, expectedCupDTOList.size());
        Mockito.when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        Mockito.when(this.userRepository.findFavoriteCupsByUserId(anyLong(),pageable)).thenReturn(expectedPageCupEntity);

        //assert
        Assertions.assertEquals(expectedPageCupDTO, userService.getFavoriteCupList(userEntity.getId(), pageable));
    }
}
