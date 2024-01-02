package org.fanaticups.fanaticupsBack.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CupRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.models.RequestBodyCreateCup;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class CupServiceTest {
    @Mock
    private CupRepository cupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CupService cupService;
    private CupDTO cupDTO;
    private CupEntity cupEntity;
    private UserEntity userEntity;
    private String imagePath;

    @BeforeEach
    void setUp() {
        this.userEntity = UserEntity.builder()
                .id(1L)
                .name("xavi")
                .email("x@x.x")
                .build();
        this.cupDTO = CupDTO.builder()
                .id(1L)
                .name("Cup1")
                .Description("Description")
                .image("image.jpg")
                .user(this.userEntity).build();
        this.cupEntity = CupEntity.builder().id(1L)
                .name("Cup1")
                .description("Description")
                .image("image.jpg")
                .user(this.userEntity)
                .build();
        this.imagePath = this.userEntity.getId() + "/" + this.cupEntity.getName() + "/";
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(this.cupRepository);
    }
    @DisplayName("FindAllCups return cupDTO with image path")
    @Test
    public void givenRequestAllCupsWhenFindAllCupsInServiceThenReturnAllCups(){
        List<CupEntity> cupListEntityForMock = new ArrayList<>();
        cupListEntityForMock.add(this.cupEntity);
        Mockito.when(this.cupRepository.findAll()).thenReturn(cupListEntityForMock);
        List<CupDTO> cupListExpected = new ArrayList<>();
        this.cupDTO.setImage(this.imagePath + "image.jpg");
        cupListExpected.add(this.cupDTO);
        List<CupDTO> result = this.cupService.findAllCups();
        Assertions.assertEquals(cupListExpected, result);
    }

    @DisplayName("FindCupById existing id return cupDTO with image path")
    @Test
    public void givenRequestExistOneCupWhenFindByIdThenReturnOptionalCup(){
        Mockito.when(this.cupRepository.findById(1L)).thenReturn(Optional.ofNullable(this.cupEntity));
        this.cupDTO.setImage(this.imagePath + "image.jpg");
        CupDTO cupDTOExpected = this.cupDTO;
        Assertions.assertEquals(Optional.of(cupDTOExpected), this.cupService.findCupById(1L));
    }

    @DisplayName("FindCupById existing id return empty optional")
    @Test
    public void givenRequestNoExistOneCupWhenFindByIdThenReturnOptionalEmpty(){
        Mockito.when(this.cupRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertEquals(Optional.empty(), this.cupService.findCupById(2L));
    }
    @DisplayName("Add cup then return the new cup")
    @Test
    public void givenCupWhenAddCupServiceThenReturnCupCreated() throws JsonProcessingException {
        Mockito.when(this.userRepository.findById(anyInt())).thenReturn(Optional.of(this.userEntity));
        Mockito.when(this.cupRepository.save(this.cupEntity)).thenReturn(this.cupEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        String cupDTOJSON_Jackson = objectMapper.writeValueAsString(this.cupDTO);
        RequestBodyCreateCup requestBodyCreateCup = new RequestBodyCreateCup("1", cupDTOJSON_Jackson);
        Assertions.assertEquals(Optional.of(this.cupDTO), this.cupService.add(requestBodyCreateCup));
    }

    @DisplayName("Exist Cup for user id with good data")
    @Test
    public void givenUserIdAndCupNameWhenAskIfCupExistThenReturnTrue(){
        Mockito.when(this.cupRepository.existsByUser_IdAndName(1L,"Cup1")).thenReturn(true);
        Assertions.assertTrue(this.cupService.existCupByUserIdAndCupName("1",this.cupDTO.getName()));
    }

    @DisplayName("Exist Cup for user id with wrong data")
    @Test
    public void givenUserIdAndWrongCupNameWhenAskIfCupExistThenReturnFalse(){
        Mockito.when(this.cupRepository.existsByUser_IdAndName(1L,"cupWrongName")).thenReturn(false);
        Assertions.assertFalse(this.cupService.existCupByUserIdAndCupName("1","cupWrongName"));
    }

    @DisplayName("Delete by cup Id")
    @Test
    public void givenCupIdWhenDeleteByIdThenReturnNothing(){
        // arrange
        Long cupId = 1L;
        // act
        this.cupService.delete(cupId);
        // assert
        Mockito.verify(this.cupRepository, Mockito.times(1)).deleteById(anyLong());
    }

}