package org.fanaticups.fanaticupsBack.security.services;

import java.util.HashMap;
import java.util.Map;

import org.fanaticups.fanaticupsBack.dao.entities.UserEntity;
import org.fanaticups.fanaticupsBack.dao.repositories.UserRepository;
import org.fanaticups.fanaticupsBack.models.JwtTokenResponseDTO;
import org.fanaticups.fanaticupsBack.models.UserLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public JwtTokenResponseDTO login(UserLoginRequestDTO userLoginRequestDTO){

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());
        this.authenticationManager.authenticate(authToken);
        System.out.println("TRAZA: " + userLoginRequestDTO.getEmail());
        UserEntity userEntity = this.userRepository.findByEmail(userLoginRequestDTO.getEmail()).get();
        String jwt = jwtService.generateToken(userEntity, generateExtraClaims(userEntity));
        return new JwtTokenResponseDTO(jwt);

    }

    private Map<String, Object> generateExtraClaims(UserEntity userEntity) {

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", userEntity.getName());
        extraClaims.put("role", userEntity.getRole().name());
        return extraClaims;
    }

    
}
