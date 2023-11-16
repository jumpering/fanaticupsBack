package org.fanaticups.fanaticupsBack.security.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationReq implements Serializable{
    
    //private static final long serialVersionUID = 1L;
    
    private String user;
    //private String email;
    private String password;

}
