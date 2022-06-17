package com.piisw.UrbanTicketSystem.infrastructure.security.adapter;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.domain.model.security.ApplicationUserDetails;
import com.piisw.UrbanTicketSystem.infrastructure.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ApplicationUserService implements UserDetailsService, SecurityRepository {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public ApplicationUserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent())
            return new ApplicationUserDetails(foundUser.get());
        else
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }

    @Override
    public User registerUser(User user, UserRole role) {
        System.out.println("registering user");
        User newUser=new User();

        if(userRepository.existsByUsername(user.getUsername())) {
            System.out.println("email already in use.");

            throw new RuntimeException(
                    String.format("email %s already in use", user.getUsername()));
        }

        newUser.setUsername(user.getUsername());
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setActive(true);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(role.name());
        newUser.setTickets(new ArrayList<>());

        return userRepository.save(newUser);
    }

    @Override
    public User updateUserCredentials(Long id, User updatedData) {
        User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setPassword(passwordEncoder.encode(updatedData.getPassword()));
        return userRepository.save(userToUpdate);
    }
}
