package org.fanaticups.fanaticupsBack.controllers;

import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.fanaticups.fanaticupsBack.security.models.AuthenticationReq;
import org.fanaticups.fanaticupsBack.security.models.RegisterReq;
import org.fanaticups.fanaticupsBack.security.models.TokenInfo;
import org.fanaticups.fanaticupsBack.security.service.JwtUtilService;
import org.fanaticups.fanaticupsBack.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtilService jwtUtilService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationReq.getEmail(), authenticationReq.getPassword()));
        final UserEntity userDetailsEntity = (UserEntity) this.userDetailsService.loadUserByUsername(authenticationReq.getEmail());
        final String jwt = this.jwtUtilService.generateToken(userDetailsEntity);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity<TokenInfo> register(@RequestBody RegisterReq registerReq ){

        String passwordCoded = bCryptPasswordEncoder.encode(registerReq.getPassword());
        UserEntity userEntity = UserEntity.builder()
                                        .name(registerReq.getName())
                                        .email(registerReq.getEmail())
                                        .password(passwordCoded)
                                        .roles("REGISTRED")//TODO a fuego quemado
                                        .build();

        if (!this.userRepository.existsByEmail(registerReq.getEmail())){
            this.userRepository.save(userEntity);
            AuthenticationReq authenticationReq = AuthenticationReq.builder()
                                                                .email(registerReq.getEmail())
                                                                .password(registerReq.getPassword())
                                                                .build();
            return this.authenticate(authenticationReq);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Message", "Email exist in data base");
        return new ResponseEntity<>(
                null, responseHeaders, HttpStatus.NOT_FOUND);
        //return ResponseEntity.notFound().build();
    }

}
