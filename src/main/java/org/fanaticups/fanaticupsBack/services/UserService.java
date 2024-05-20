package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CupService cupService;

    private ModelMapper modelMapper  = new ModelMapper();

    @Value("${apiRootImagesMinio}")
    private String imageMinioUrl;

    public boolean setCupToFavorites(UserEntity userEntity, CupDTO cupDTO) {
        List<CupEntity> listOfCupEntityFavorites = userEntity.getFavoriteCupList();
        CupEntity cupEntity = this.modelMapper.map(cupDTO, CupEntity.class);
        boolean isFavorite = listOfCupEntityFavorites.contains(cupEntity);
        if(!isFavorite){
            listOfCupEntityFavorites.add(cupEntity);
            userEntity.setFavoriteCupList(listOfCupEntityFavorites);
            this.userRepository.save(userEntity);
            return true; //cup added to favorite list
        } else {
            listOfCupEntityFavorites.remove(cupEntity);
            userEntity.setFavoriteCupList(listOfCupEntityFavorites);
            this.userRepository.save(userEntity);
            return false; //cup removed to favorite list
        }

    }

    public Optional<UserEntity> findUserById(Long userId) {
        return this.userRepository.findById(userId);
    }

    public boolean isCupFavorite(Long userId, Long cupId) {
        Optional<UserEntity> userEntity = this.userRepository.findById(userId);
        if (userEntity.isPresent()) {
            List<CupEntity> cupEntityList = userEntity.get().getFavoriteCupList();
            for (CupEntity cupEntity : cupEntityList) {
                if (cupId.equals(cupEntity.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Page<CupDTO> getFavoriteCupList(Long userId, Pageable pageable) {
        Optional<UserEntity> userEntity = this.userRepository.findById(userId);
        Page<CupDTO> pageCupDTO = null;
        if (userEntity.isPresent()) {
            Page<CupEntity> pageCupEntity = this.userRepository.findFavoriteCupsByUserId(userId, pageable);
            List<CupDTO> cupDTOListWithImageURL = new ArrayList<>();
            for(CupEntity cupEntity : pageCupEntity.getContent()) {
                CupDTO cupDTO = this.modelMapper.map(cupEntity, CupDTO.class);
                this.cupService.setImageUrl(cupDTO);
                cupDTOListWithImageURL.add(cupDTO);
            }
            pageCupDTO = new PageImpl<>(cupDTOListWithImageURL, pageable, cupDTOListWithImageURL.size());
        }
        return pageCupDTO;
    }
}
