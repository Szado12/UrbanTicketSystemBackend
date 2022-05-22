package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.model.security.ApplicationUserDetails;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.security.adapter.ApplicationUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {ApplicationUserService.class})
public class ApplicationUserServiceTests {
    @InjectMocks
    ApplicationUserService applicationUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    public void shouldReturnUserDetailsByUsername() {
        String username = "user@gmail.com";
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("1234");
        testUser.setRole(String.valueOf(UserRole.CLIENT));
        testUser.setActive(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(testUser));
        UserDetails createdUser = applicationUserService.loadUserByUsername(username);
        assertThat(createdUser.getUsername()).isSameAs(testUser.getUsername());
        assertThat(createdUser.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnUserOnRegister() {
        String username = "user@gmail.com";
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("1234");

        User registeredUser = new User(0, username, "1234", "CLIENT", true, null, null, new ArrayList<>());

        when(userRepository.save(any(User.class))).thenReturn(registeredUser);
        when(passwordEncoder.encode("1234")).thenReturn("1234");
        when(userRepository.existsByUsername(username)).thenReturn(false);
        User createdUser = applicationUserService.registerUser(testUser, UserRole.CLIENT);
        assertThat(createdUser.getUsername()).isSameAs(testUser.getUsername());
        assertThat(createdUser.getPassword()).isSameAs(testUser.getPassword());
        assertThat(createdUser.getRole()).isSameAs(String.valueOf(UserRole.CLIENT));
        assertThat(createdUser.isActive()).isSameAs(true);
    }
}
