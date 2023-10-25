package org.fanaticups.fanaticupsBack.security.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationReq implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String user;
    private String password;

    
    public AuthenticationReq(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void setUser(String user){
        this.user = user;
    }

    public String getUser(){
        return this.user;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }
}
