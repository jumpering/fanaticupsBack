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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userOpt = this.userRepository.findByName(username);
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
