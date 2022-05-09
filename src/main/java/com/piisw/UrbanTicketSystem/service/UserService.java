package com.piisw.UrbanTicketSystem.service;

import com.piisw.UrbanTicketSystem.jwt.JwtTokenProvider;
import com.piisw.UrbanTicketSystem.model.Client;
import com.piisw.UrbanTicketSystem.model.User;
import com.piisw.UrbanTicketSystem.repository.ClientRepository;
import com.piisw.UrbanTicketSystem.repository.UserRepository;
import com.piisw.UrbanTicketSystem.security.ApplicationUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, ClientRepository clientRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }


    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return tokenProvider.generateToken(authentication);
    }

    public User registerUser(User user, ApplicationUserRole role) {
        System.out.println("registering user");
        Client newClient=new Client();

        if(userRepository.existsByUsername(user.getUsername())) {
            System.out.println("username already exists.");

            throw new RuntimeException(
                    String.format("username %s already exists", user.getUsername()));
        }

        newClient.setUsername(user.getUsername());
        newClient.setName(user.getName());
        newClient.setSurname(user.getSurname());
        newClient.setActive(true);
        newClient.setPassword(passwordEncoder.encode(user.getPassword()));
        newClient.setRole(role.name());

        return clientRepository.save(newClient);
    }

    public List<User> findAll() {
        System.out.println("retrieving all users");
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        System.out.println("retrieving user");
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        System.out.println("retrieving user");
        return userRepository.findById(id);
    }
}
