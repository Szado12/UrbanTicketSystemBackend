package com.piisw.UrbanTicketSystem.infrastructure.security.adapter;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.security.model.ApplicationUserDetails;
import com.piisw.UrbanTicketSystem.infrastructure.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        Optional<User> foundUser = userRepository.findByEmail(username);
        if (foundUser.isPresent())
            return new ApplicationUserDetails(foundUser.get());
        else
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return tokenProvider.generateToken(authentication);
    }

    public User registerUser(User user, UserRole role) {
        System.out.println("registering user");
        User newUser=new User();

        if(userRepository.existsByEmail(user.getEmail())) {
            System.out.println("email already in use.");

            throw new RuntimeException(
                    String.format("email %s already in use", user.getEmail()));
        }

        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setActive(true);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(role.name());

        return userRepository.save(newUser);
    }
}
