package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaUserService.class})
public class UserServiceTests {
    @InjectMocks
    JpaUserService jpaUserService;

    @Mock
    JpaUserRepository jpaUserRepository;

    @Test
    public void shouldReturnUserOnSave() {
        User testUser = new User();
        testUser.setUsername("user@gmail.com");
        testUser.setPassword("1234");
        testUser.setTickets(new ArrayList<>());

        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setId(0L);
        testUserEntity.setUsername("user@gmail.com");
        testUserEntity.setPassword("1234");
        testUserEntity.setTickets(new ArrayList<>());

        when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(testUserEntity);
        User createdUser = jpaUserService.save(testUser);
        assertThat(createdUser.getUsername()).isSameAs(testUser.getUsername());
        assertThat(createdUser.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnUserByUsername() {
        String username = "user@gmail.com";
        User testUser = new User();
        testUser.setUsername("user@gmail.com");
        testUser.setPassword("1234");
        testUser.setTickets(new ArrayList<>());

        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setId(0L);
        testUserEntity.setUsername("user@gmail.com");
        testUserEntity.setPassword("1234");
        testUserEntity.setTickets(new ArrayList<>());

        when(jpaUserRepository.findByUsername(username)).thenReturn(Optional.of(testUserEntity));
        User user = jpaUserService.findByUsername(username).get();
        assertThat(user.getUsername()).isSameAs(testUser.getUsername());
        assertThat(user.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnUserById() {
        String username = "user@gmail.com";
        User testUser = new User();
        testUser.setUsername("user@gmail.com");
        testUser.setPassword("1234");
        testUser.setTickets(new ArrayList<>());

        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setId(0L);
        testUserEntity.setUsername("user@gmail.com");
        testUserEntity.setPassword("1234");
        testUserEntity.setTickets(new ArrayList<>());

        when(jpaUserRepository.findById(0L)).thenReturn(Optional.of(testUserEntity));
        User user = jpaUserService.findById(0L).get();
        assertThat(user.getUsername()).isSameAs(testUser.getUsername());
        assertThat(user.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnIfExistsByUsername() {
        String username = "user@gmail.com";
        User testUser = new User();
        testUser.setUsername("user@gmail.com");
        testUser.setPassword("1234");
        testUser.setTickets(new ArrayList<>());

        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setId(0L);
        testUserEntity.setUsername("user@gmail.com");
        testUserEntity.setPassword("1234");
        testUserEntity.setTickets(new ArrayList<>());

        when(jpaUserRepository.existsByUsername(username)).thenReturn(true);
        assert(jpaUserService.existsByUsername(username));
    }
}
