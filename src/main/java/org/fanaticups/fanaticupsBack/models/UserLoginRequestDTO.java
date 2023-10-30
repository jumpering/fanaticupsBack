package org.fanaticups.fanaticupsBack.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class UserLoginRequestDTO {
     
    private String email;
    private String password;

}
