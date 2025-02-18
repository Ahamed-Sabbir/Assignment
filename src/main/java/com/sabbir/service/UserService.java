package com.sabbir.service;

import com.sabbir.dto.UserDto;
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

    private UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setUsername(user.getUsername());
        userDto.setActive(user.isActive());
        if(user.getCreatedBy() != null) userDto.setCreatedBy(user.getCreatedBy().getUsername());
        else userDto.setCreatedBy(null);
        userDto.setRole(user.getAuthority().getAuthority());
        return userDto;
    }

    public UserDto findUserForInfo(String username){
        User user = userRepo.findByUsernameIgnoreCase(username).get();
        return convertUserToUserDto(user);
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

    public Page<UserDto> getUsers(User loggedInBy, Pageable pageable){
        Page<User> users;
        if(loggedInBy.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")){
            users = userRepo.findAll(pageable);
        }
        else users = userRepo.findByCreatedBy(loggedInBy, pageable);
        return users.map(this::convertUserToUserDto);
    }

    public UserDto searchInfo(User loggedInBy, String username) {
        Optional<User> userOptional = userRepo.findByUsernameIgnoreCase(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")) return null;
            if(user.getCreatedBy().getId().equals(loggedInBy.getId()) ||
                    loggedInBy.getAuthority().getAuthority().equals("ROLE_SUPREME_ADMIN")) return convertUserToUserDto(user);
        }
        return null;
    }

}
