package org.fanaticups.fanaticupsBack.services;

import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.UserEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.UserRepository;
import org.fanaticups.fanaticupsBack.models.UserRegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

@Service
public class UserService {

    // @Autowired
    // UserRepository userRepository;

    // private final ModelMapper modelMapper = new ModelMapper();

    // public Optional<RegisterReq> createUser( RegisterReq registerReq ){

    //     Optional<RegisterReq> optionalRegisterReqDTO = Optional.empty();
    //     if (!this.userRepository.existByEmail(registerReq.getEmail())){
    //         UserEntity userEntity = this.modelMapper.map(optionalRegisterReqDTO, UserEntity.class);
    //         userEntity = this.userRepository.save(userEntity);
    //         RegisterReq registerReqDTO = this.modelMapper.map(userEntity, RegisterReq.class);
    //         optionalRegisterReqDTO = Optional.of(registerReqDTO);
    //     }
    //     return optionalRegisterReqDTO;
    // }
    
}
