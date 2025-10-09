package com.edumate.backend.service;

import com.edumate.backend.dto.AuthDtos;
import com.edumate.backend.entity.sql.User;
import com.edumate.backend.repository.sql.UserRepository;
import com.edumate.backend.security.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenService jwtTokenService,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        String username = request.email; // use email as username for simplicity
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already in use");
        }

        User user = new User();
        user.setEmail(request.email);
        user.setUsername(username);
        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        user.setPassword(passwordEncoder.encode(request.password));
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email);
        String token = jwtTokenService.generateToken(userDetails);
        return buildAuthResponse(user, token);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenService.generateToken(userDetails);
        User user = userRepository.findByEmail(request.email).orElseThrow();
        return buildAuthResponse(user, token);
    }

    public boolean verify(String token) {
        String username = jwtTokenService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenService.isTokenValid(token, userDetails);
    }

    private AuthDtos.AuthResponse buildAuthResponse(User user, String token) {
        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.token = token;
        AuthDtos.UserPayload payload = new AuthDtos.UserPayload();
        payload.id = user.getUserId();
        payload.firstName = user.getFirstName();
        payload.lastName = user.getLastName();
        payload.email = user.getEmail();
        response.user = payload;
        return response;
    }
}


