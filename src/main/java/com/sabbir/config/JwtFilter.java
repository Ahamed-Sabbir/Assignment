package com.sabbir.config;

import com.sabbir.service.UserService;
import com.sabbir.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // if token is present in the request
        // validate the token
        // proceed as usual
        try {
            String jwt = parseJwt(request);
            String username = null;

            if (jwt != null) {
                try {
                    // Extract username
                    username = jwtUtil.extractUsername(jwt);
                } catch (ExpiredJwtException e) {
                    System.out.println("Token expired for user: " + e.getClaims().getSubject());
                    // Extract subject manually from expired token
                    username = e.getClaims().getSubject();
                }

                if (jwtUtil.validateJwtToken(jwt)) {
                    List<String> authorities = jwtUtil.extractAuthorities(jwt);
                    List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                            .map(SimpleGrantedAuthority::new).toList();
                    UserDetails userDetails = new User(username, "", grantedAuthorities);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {

                    // updating users active status to inactive or false
                    System.out.println("Token is invalid or expired for user: " + username);
                    Optional<com.sabbir.model.User> userOptional = userService.findUserByUsername(username);
                    userOptional.ifPresent(user -> {
                        user.setActive(false);
                        userService.saveUser(user);
                        System.out.println("User " + user.getUsername() + " is now inactive.");
                    });
                }
            }
        } catch (Exception ex) {
            System.out.println("JWT Filter Error: " + ex.getMessage());
        }

        // if toke is not present in the request
        // process as usual
        filterChain.doFilter(request, response);
    }


    /**
     * Parses the JWT token from the Authorization header.
     *
     * @param request the incoming HttpServletRequest.
     * @return the extracted JWT token or null if not found.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
