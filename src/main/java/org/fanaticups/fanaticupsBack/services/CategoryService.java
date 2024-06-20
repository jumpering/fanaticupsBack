package org.fanaticups.fanaticupsBack.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.fanaticups.fanaticupsBack.dao.entities.CategoryEntity;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.CategoryRepository;
import org.fanaticups.fanaticupsBack.models.CategoryDTO;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CupService cupService;

    private final ModelMapper modelMapper = new ModelMapper();
    //private final ObjectMapper objectMapper = new ObjectMapper(); //Jackson mapper
    @Value("${apiRootCategories}")
    private String imageMinioUrl;

    public List<CategoryDTO> findAll(){
        List<CategoryEntity> categoryEntityList = this.categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = categoryEntityList.stream().map(element -> this.modelMapper.map(element, CategoryDTO.class)).toList();
        categoryDTOList.forEach(this::setImageUrl);
        return categoryDTOList;
    }

    public CategoryDTO setImageUrl(CategoryDTO categoryDTO) {
        categoryDTO.setImage(this.imageMinioUrl + categoryDTO.getImage());
        return categoryDTO;
    }

    public Optional<CategoryDTO> save(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = this.modelMapper.map(categoryDTO, CategoryEntity.class);
        CategoryEntity categoryEntitySaved = this.categoryRepository.save(categoryEntity);
        CategoryDTO categoryDTOSaved = this.modelMapper.map(categoryEntitySaved, CategoryDTO.class);
        return Optional.of(categoryDTOSaved);
    }

    public Page<CupDTO> findCupsByCategoryId(Long id, Pageable pageable) {
        Page<CupEntity> pageCupEntity = this.categoryRepository.findCupsByCategoryId(id, pageable);
        //List<CupDTO> cupDTOList = new ArrayList<>();
        Page<CupDTO> pageCupDTO = pageCupEntity.map(element -> this.modelMapper.map(element, CupDTO.class));
        pageCupDTO.forEach(this.cupService::setImageUrl);

//        for (CupEntity cupEntity : cupEntityList) {
//            CupDTO cupDTO = this.modelMapper.map(cupEntity, CupDTO.class);
//            cupDTOList.add(cupDTO);
//        }
        return pageCupDTO;
    }
}
