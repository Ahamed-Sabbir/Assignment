package com.sabbir.service;

import com.sabbir.model.Authority;
import com.sabbir.model.User;
import com.sabbir.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUsernameIgnoreCase(username);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            Set<GrantedAuthority> authorities = new HashSet<>();
            Authority authority = user.getAuthority();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            authorities.add(simpleGrantedAuthority);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

        }
        throw new UsernameNotFoundException("No user found with" + username + "username");
    }
}
