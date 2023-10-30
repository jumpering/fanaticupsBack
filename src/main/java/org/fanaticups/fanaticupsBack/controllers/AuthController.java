package org.fanaticups.fanaticupsBack.controllers;

import java.util.Optional;

import org.fanaticups.fanaticupsBack.models.JwtTokenResponseDTO;
import org.fanaticups.fanaticupsBack.models.UserLoginRequestDTO;
import org.fanaticups.fanaticupsBack.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // @Autowired
    // AuthenticationManager authenticationManager;

    // @Autowired
    // UserDetailsService userDetailsService;

    // @Autowired
    // JwtUtilService jwtUtilService;

    // @Autowired
    // UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    // @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDTO> login(
            @RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {
        System.out.println("TRAZA: " + userLoginRequestDTO);
        JwtTokenResponseDTO jwtDTO = this.authenticationService.login(userLoginRequestDTO);
        return ResponseEntity.ok(jwtDTO);

        // this.authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword())
        // );
        // final UserDetails userDetails =
        // this.userDetailsService.loadUserByUsername(userLoginRequestDTO.getEmail());
        // final String jwt = this.jwtUtilService.generateToken(userDetails);
        // HttpHeaders responseHeaders = new HttpHeaders();
        // responseHeaders.set("Authorization", "Bearer " + jwt);
        // //return ResponseEntity.ok().headers(responseHeaders).body(new
        // TokenInfo(jwt));
        // return ResponseEntity.ok().headers(responseHeaders).build();
    }

    // @PostMapping("/register")
    // public ResponseEntity<TokenInfo> register ( @RequestBody RegisterReq
    // registerReq ){
    // Optional<RegisterReq> optRegisterReq =
    // this.userService.createUser(registerReq);
    // if (optRegisterReq.isEmpty()){
    // return ResponseEntity.status(HttpStatus.CONFLICT).build();
    // }
    // registerReq = optRegisterReq.get();

    // new AuthenticationReq();
    // AuthenticationReq newUserRequest = AuthenticationReq
    // .builder()
    // .user(registerReq.getEmail())
    // .password(registerReq.getPassword())
    // .build();

    // //return this.authenticate(newUserRequest);
    // return null;
    // }

}
