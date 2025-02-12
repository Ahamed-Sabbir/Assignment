package com.sabbir.Controller;

import com.sabbir.model.User;
import com.sabbir.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ListController {
    private final UserService userService;

    public ListController(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param loggedIn - takes loggedIn user
     * @param pageable - pageable for pagination
     * @return - response
     */

    @GetMapping("/list")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetails loggedIn, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Page<User> users = userService.getUsers(userService.findUserByUsername(loggedIn.getUsername()).get(), pageable);
        response.put("users", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param loggedIn - takes loggedIn user
     * @param username - takes username to search. it can be uppercase, lowercase or mix of both
     * @return - response
     */

    @GetMapping("/user")
    public ResponseEntity<?> getSearchedUser(@AuthenticationPrincipal UserDetails loggedIn, @RequestParam String username){
        Map<String, Object> response = new HashMap<>();
        User user = userService.searchInfo(userService.findUserByUsername(loggedIn.getUsername()).get(), username);
        if(user == null) {
            response.put("message", "user not exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    /**
     *
     * @param loggedIn - - takes loggedIn user
     * @return - response
     */

    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails loggedIn){
        Map<String, Object> response = new HashMap<>();
        User user = userService.findUserByUsername(loggedIn.getUsername()).get();
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

}
