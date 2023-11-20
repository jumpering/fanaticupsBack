package org.fanaticups.fanaticupsBack.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;

import java.util.List;

@Data
public class UserDTO {

    private String name;

    private String email;

    private String password;

    private String roles;

    private List<CupDTO> cups;
}
