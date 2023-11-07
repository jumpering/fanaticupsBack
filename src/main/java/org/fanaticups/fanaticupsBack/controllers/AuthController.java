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
@RequestMapping
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    // @Autowired
    // UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtilService jwtUtilService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    // @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationReq.getUser(), authenticationReq.getPassword()));
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationReq.getUser());
        final String jwt = this.jwtUtilService.generateToken(userDetails);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);
        // return ResponseEntity.ok().headers(responseHeaders).body(new TokenInfo(jwt));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/register")
    public ResponseEntity<TokenInfo> register(@RequestBody RegisterReq registerReq ){

        String passwordCoded = bCryptPasswordEncoder.encode(registerReq.getPassword());
        UserEntity userEntity = UserEntity.builder()
                                        .email(registerReq.getEmail())
                                        .name(registerReq.getName())
                                        .password(passwordCoded)
                                        .roles("ADMIN")//TODO a fuego quemado
                                        .build();

        if (!this.userRepository.existsByEmail(registerReq.getEmail())){
            this.userRepository.save(userEntity);
            AuthenticationReq authenticationReq = AuthenticationReq.builder()
                                                                .user(registerReq.getEmail())
                                                                .password(registerReq.getPassword())
                                                                .build();
            return this.authenticate(authenticationReq);
        }
        //comment
        return ResponseEntity.notFound().build();
    }

}
