package com.htecgroup.flightadvisor.controller;

import com.htecgroup.flightadvisor.security.jwt.TokenProvider;
import com.htecgroup.flightadvisor.service.UserService;
import com.htecgroup.flightadvisor.service.dto.AuthenticateRequest;
import com.htecgroup.flightadvisor.service.dto.AuthenticateResponse;
import com.htecgroup.flightadvisor.service.dto.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public AuthenticationController(
            UserService userService,
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest
    ) {
        userService.registerUser(registerUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authorize(
            @Valid @RequestBody AuthenticateRequest authenticateRequest
    ) {
        var username = authenticateRequest.getUsername();
        var password = authenticateRequest.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password
        );

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        return new ResponseEntity<>(new AuthenticateResponse(jwt), HttpStatus.OK);
    }

}
