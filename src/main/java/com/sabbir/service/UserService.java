package com.sabbir.service;

import com.sabbir.model.Authority;
import com.sabbir.model.User;
import com.sabbir.repository.AuthorityRepo;
import com.sabbir.repository.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final AuthorityRepo authorityRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(AuthorityRepo authorityRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.authorityRepo = authorityRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByUsername(String username){
        return userRepo.findByUsernameIgnoreCase(username);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void saveAdminUser(User user, User createdBy) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = authorityRepo.findByAuthority("ROLE_ADMIN");
        user.setAuthority(authority);
        user.setCreatedBy(createdBy);
        userRepo.save(user);
    }

    public void saveRegularUser(User user, User createdBy) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = authorityRepo.findByAuthority("ROLE_USER");
        user.setAuthority(authority);
        user.setCreatedBy(createdBy);
        userRepo.save(user);
    }

    public Page<User> getUsers(User loggedInBy, Pageable pageable){
        if(loggedInBy.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")){
            return userRepo.findAll(pageable);
        }
        return userRepo.findByCreatedBy(loggedInBy, pageable);
    }

    public User searchInfo(User loggedInBy, String username) {
        Optional<User> userOptional = userRepo.findByUsernameIgnoreCase(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")) return null;
            if(user.getCreatedBy().getId().equals(loggedInBy.getId()) ||
                    loggedInBy.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")) return user;
        }
        return null;
    }

}
