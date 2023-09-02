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
    private CupRepository CupRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private String imagePath = "assets/images/";

    public List<CupDTO> findAllCups(){
        List<CupEntity> cupsEntities;
        List<CupDTO> cupsDTO = new ArrayList<CupDTO>();
        cupsEntities = this.CupRepository.findAll();
        cupsEntities.forEach(element -> element.setImage(this.imagePath + element.getImage()));
        cupsEntities.forEach(element -> cupsDTO.add(
            this.modelMapper.map(element, CupDTO.class)
        ));
        return cupsDTO;
    }

    public CupDTO findCupById(Long id){
        Optional<CupEntity> cupEntity = this.CupRepository.findById(id);
        if(cupEntity.isPresent()){
            CupEntity cup = cupEntity.get();
            String cupImageName = cup.getImage();
            cup.setImage(this.imagePath + cupImageName);
            CupDTO cupDTO = this.modelMapper.map(cup, CupDTO.class);
            return cupDTO;
        }
        return null;
    }
}
