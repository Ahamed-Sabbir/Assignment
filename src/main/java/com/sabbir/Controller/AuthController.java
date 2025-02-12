package com.sabbir.Controller;

import com.sabbir.model.User;
import com.sabbir.service.UserService;
import com.sabbir.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    /**
     *
     * @param user takes username and password
     * @return a JWT token
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // check if username + password is correct
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            //create jwt token
            String token = jwtUtil.generateToken(userDetails);
            List<String> myRole = jwtUtil.extractAuthorities(token);
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("Role", myRole);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception ex){
            response.put("message", "Invalid Credential");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     *
     * @param user - takes user information
     * @param createdBy - takes current logged in user
     * @return response
     */

    @PostMapping("/register/admin")
    public ResponseEntity<?> addAdmin(@RequestBody User user, @AuthenticationPrincipal UserDetails createdBy){
        Map<String, Object> response = new HashMap<>();
        if(userService.findUserByUsername(user.getUsername()).isPresent()){
            response.put("message", "username already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        try {
            userService.saveAdminUser(user, userService.findUserByUsername(createdBy.getUsername()).get());
        } catch (Exception ex){
            response.put("message", "Something Unexpected Happened, Try Again");
            response.put("errorBody", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("message", "registration successful.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     *
     * @param user - takes user information
     * @param createdBy - takes current logged in user
     * @return response
     */

    @PostMapping("/register/user")
    public ResponseEntity<?> addUser(@RequestBody User user, @AuthenticationPrincipal UserDetails createdBy){
        Map<String, Object> response = new HashMap<>();
        if(userService.findUserByUsername(user.getUsername()).isPresent()){
            response.put("message", "username already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        try {
            userService.saveRegularUser(user, userService.findUserByUsername(createdBy.getUsername()).get());
        } catch (Exception ex){
            response.put("message", "Something Unexpected Happened, Try Again");
            response.put("errorBody", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("message", "registration successful.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
