package org.fanaticups.fanaticupsBack.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {

    private String name;
    private String email;
    private String password;

    
}
