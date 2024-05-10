package org.fanaticups.fanaticupsBack.services;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper  = new ModelMapper();

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

    public List<CupDTO> getFavoriteCupList(Long userId) {
        List<CupDTO> cupDTOList = new ArrayList<CupDTO>();
        Optional<UserEntity> userEntity = this.userRepository.findById(userId);
        if (userEntity.isPresent()) {
            cupDTOList = userEntity.get().getFavoriteCupList()
                    .stream().map(element -> this.modelMapper.map(element, CupDTO.class))
                    .toList();
        }
        return cupDTOList;
    }
}
