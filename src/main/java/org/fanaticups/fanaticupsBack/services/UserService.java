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

    private ModelMapper modelMapper  = new ModelMapper();

    @Value("${apiRootImagesMinio}")
    private String imageMinioUrl;

    public void setCupToFavorites(UserEntity userEntity, CupDTO cupDTO) {
        List<CupEntity> listOfCupEntityFavorites = userEntity.getFavoriteCupList();
        CupEntity cupEntity = this.modelMapper.map(cupDTO, CupEntity.class);
        listOfCupEntityFavorites.add(cupEntity);
        userEntity.setFavoriteCupList(listOfCupEntityFavorites);
        this.userRepository.save(userEntity);
    }

    public Optional<UserEntity> findUserById(Long userId) {
        return this.userRepository.findById(userId);
    }

    public Page<CupDTO> getFavoriteCupList(Long userId, Pageable pageable) {
        //List<CupDTO> cupDTOList = new ArrayList<CupDTO>();
        Optional<UserEntity> userEntity = this.userRepository.findById(userId);
        Page<CupDTO> pageCupDTO = null;
        if (userEntity.isPresent()) {
            Page<CupEntity> pageCupEntity = this.userRepository.findFavoriteProductsByUserId(userId, pageable);
            //modifico imagen url
            List<CupDTO> cupDTOListWithImageURL = new ArrayList<>();
            for(CupEntity cupEntity : pageCupEntity.getContent()) {
                CupDTO cupDTO = this.modelMapper.map(cupEntity, CupDTO.class);
                this.setImageUrl(cupDTO);
                cupDTOListWithImageURL.add(cupDTO);
            }
            pageCupDTO = new PageImpl<>(cupDTOListWithImageURL, pageable, cupDTOListWithImageURL.size());
            //pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));

//            String imagePath = this.imageMinioUrl + cupDTO.getUser().getId() + "/" + cupDTO.getId() + "/";
//            cupDTO.setImage(imagePath + cupDTO.getImage());
        }
        return pageCupDTO;
    }

    public CupDTO setImageUrl(CupDTO cupDTO) {
        String imagePath = this.imageMinioUrl + cupDTO.getUser().getId() + "/" + cupDTO.getId() + "/";
        cupDTO.setImage(imagePath + cupDTO.getImage());
        return cupDTO;
    }
}
