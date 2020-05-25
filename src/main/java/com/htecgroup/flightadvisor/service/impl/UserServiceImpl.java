package com.htecgroup.flightadvisor.service.impl;

import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.domain.Authority;
import com.htecgroup.flightadvisor.exception.UsernameAlreadyTakenException;
import com.htecgroup.flightadvisor.repository.AuthorityRepository;
import com.htecgroup.flightadvisor.repository.UserRepository;
import com.htecgroup.flightadvisor.security.AuthoritiesConstants;
import com.htecgroup.flightadvisor.service.UserService;
import com.htecgroup.flightadvisor.service.dto.RegisterUserRequest;
import com.htecgroup.flightadvisor.service.mapper.RegisterUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component("userDetailsService")
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RegisterUserMapper registerUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            AuthorityRepository authorityRepository,
            RegisterUserMapper registerUserMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.registerUserMapper = registerUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterUserRequest request) {
        userRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new UsernameAlreadyTakenException("Username already taken");
                });

        Authority userAuthority = authorityRepository.findById(AuthoritiesConstants.USER)
                .orElseThrow(() -> new RuntimeException("User authority not found"));

        ApplicationUser applicationUser = registerUserMapper.toUser(request);
        applicationUser.getAuthorities()
                .add(userAuthority);
        applicationUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(applicationUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneWithAuthoritiesByUsername(lowercaseLogin)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

    }

    private User createSpringSecurityUser(ApplicationUser applicationUser) {
        List<GrantedAuthority> grantedAuthorities = applicationUser.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())) // NOSONAR
                .collect(Collectors.toList());
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), grantedAuthorities);
    }
}
