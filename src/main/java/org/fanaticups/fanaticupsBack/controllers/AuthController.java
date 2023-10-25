package org.fanaticups.fanaticupsBack.controllers;

import org.fanaticups.fanaticupsBack.security.models.AuthenticationReq;
import org.fanaticups.fanaticupsBack.security.models.SignupRequest;
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

    //@Autowired
    //UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    JwtUtilService jwtUtilService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq ){

        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationReq.getUser(), authenticationReq.getPassword())
        );
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationReq.getUser());
        final String jwt = this.jwtUtilService.generateToken(userDetails);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);
        //return ResponseEntity.ok().headers(responseHeaders).body(new TokenInfo(jwt));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
    
}
