package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CupRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CupServiceTest {
    @Mock
    private CupRepository cupRepository;
    @InjectMocks
    private CupService cupService;

    private CupDTO cupOne;
    private CupDTO cupTwo;
    private CupDTO cupThree;

    private CupEntity cupOneEntity;
    private UserEntity userEntity;
    @BeforeEach
    void setUp() {
        this.userEntity = UserEntity.builder()
                .id(1L)
                .name("xavi")
                .email("x@x.x")
                .build();
        this.cupOne = CupDTO.builder()
                .id(1L)
                .name("Cup1")
                .Description("Description")
                .image("")
                .user(this.userEntity).build();
        this.cupOneEntity = CupEntity.builder().id(1L)
                .name("Cup1")
                .description("Description")
                .image("image.jpg")
                .user(this.userEntity)
                .build();
    }

    @Test
    public void givenRequestAllCupsWhenFindAllCupsInServiceThenReturnAllCups(){
        List<CupEntity> cupListEntityForMock = new ArrayList<>();
        cupListEntityForMock.add(this.cupOneEntity);
        Mockito.when(this.cupRepository.findAll()).thenReturn(cupListEntityForMock);
        String imagePath = this.userEntity.getId() + "/" + this.cupOneEntity.getName() + "/";
        List<CupDTO> cupListExpected = new ArrayList<>();
        this.cupOne.setImage(imagePath + "image.jpg");
        cupListExpected.add(this.cupOne);
        List<CupDTO> result = this.cupService.findAllCups();
        Assertions.assertEquals(cupListExpected, result);
    }
}