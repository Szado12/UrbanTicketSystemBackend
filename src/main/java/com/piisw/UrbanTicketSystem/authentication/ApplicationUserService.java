package com.piisw.UrbanTicketSystem.authentication;

import com.piisw.UrbanTicketSystem.model.User;
import com.piisw.UrbanTicketSystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository applicationUserRepository;

    public ApplicationUserService(UserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = applicationUserRepository.findByUsername(username);
        if (foundUser.isPresent())
            return new ApplicationUserDetails(foundUser.get());
        else
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }
}
