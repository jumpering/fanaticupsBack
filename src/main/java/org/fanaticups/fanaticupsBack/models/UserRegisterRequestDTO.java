package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDTO {

    private String name;
    private String email;
    private String password;
    
}
