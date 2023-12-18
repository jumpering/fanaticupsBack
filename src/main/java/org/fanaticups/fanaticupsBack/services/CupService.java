package org.fanaticups.fanaticupsBack.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CupRepository;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.modelmapper.ModelMapper;

@Service
public class CupService {

    @Autowired
    private CupRepository cupRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    //private String imagePath = "assets/images/";
    //private String imagePath = "///Users/xaviergomezcanals/Documents/Projects/fanaticups/images/";

    public List<CupDTO> findAllCups(){
        List<CupEntity> cupsEntities;
        List<CupDTO> cupsDTO = new ArrayList<CupDTO>();
        cupsEntities = this.cupRepository.findAll();
        cupsEntities.forEach(element -> {
            String imagePath = element.getUser().getId() + "/" + element.getName() + "/";
            element.setImage(imagePath + element.getImage());
        });
        cupsEntities.forEach(element -> cupsDTO.add(
            this.modelMapper.map(element, CupDTO.class)
        ));
        return cupsDTO;
    }

    public CupDTO findCupById(Long id){
        Optional<CupEntity> cupEntity = this.cupRepository.findById(id);
        if(cupEntity.isPresent()){
            CupEntity cup = cupEntity.get();
            String imagePath = cup.getUser().getId() + "/" + cup.getName() + "/";
            cup.setImage(imagePath + cup.getImage());
            return this.modelMapper.map(cup, CupDTO.class);
        }
        return null;
    }

    public CupDTO add(CupDTO cupDTO){
        boolean exist = this.cupRepository.existsByName(cupDTO.getName());
        if(!exist){
            CupEntity cupEntity = this.modelMapper.map(cupDTO ,CupEntity.class);
            this.cupRepository.save(cupEntity);
            cupDTO = this.modelMapper.map(cupEntity, CupDTO.class);
            return cupDTO;
        }
            return null;
    }

//    public Optional<List<CupDTO>> findAllCupsFromUser(Long id) {
//        List<CupEntity> cupListEntity = this.cupRepository.findAllByUserId(id);
//        if(!cupListEntity.isEmpty()){
//            List<CupDTO> cupListDTO = cupListEntity.stream()
//                    .map(element -> this.modelMapper.map(element, CupDTO.class))
//                    .toList();
//            return Optional.of(cupListDTO);
//        }
//        return Optional.empty();
//    }

    public Boolean existCupByUserIdAndCupName(String userId, String name) {
        return this.cupRepository.existsByUser_IdAndName(Long.valueOf(userId), name);
    }

    public void delete(Long id) {
        this.cupRepository.deleteById(id);
    }
}
