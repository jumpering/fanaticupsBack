package org.fanaticups.fanaticupsBack.security.service;

import java.util.Optional;

import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.fanaticups.fanaticupsBack.security.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    //public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //Optional<UserEntity> userOpt = this.userRepository.findByName(username);
        Optional<UserEntity> userOpt = this.userRepository.findOneByEmail(email);
        UserEntity user = userOpt.get();
        if(user == null){
            //throw new UsernameNotFoundException(username);
            throw new UsernameNotFoundException(email);
        }


        return UserEntity.builder()
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();


//        return User
//                //.withUsername(username)
//                .withUsername(email)
//                .password(user.getPassword())
//                //.roles(user.roles().toArray(new String[0]))
//                //.roles("ADMIN")
//                .roles(user.getRoles())
//                .build();

    }
    
}
