package org.fanaticups.fanaticupsBack.models;

import org.fanaticups.fanaticupsBack.security.dao.UserEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CupDTO {

    private Long id;
    private String name;
    private String origin;
    private String Description;
    private String image;
    private Double price;
//    private String owner;

//    @JsonIgnore
    private UserEntity user;
}
