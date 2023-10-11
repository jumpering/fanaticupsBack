package org.fanaticups.fanaticupsBack.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userOpt = this.userRepository.findById(1);
        UserEntity user = userOpt.get();
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return User
                .withUsername(username)
                .password(user.getPassword())
                //.roles(user.roles().toArray(new String[0]))
                .roles("ADMIN")
                .build();  
    }
    
}
