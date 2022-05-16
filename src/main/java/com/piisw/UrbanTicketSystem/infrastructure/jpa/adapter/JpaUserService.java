package com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class JpaUserService implements UserRepository {
    private final JpaUserRepository userRepository;

    @Autowired
    public JpaUserService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(mapEntityToUser(userRepository.findByUsername(username).orElse(null)));
    }

    @SneakyThrows
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(mapEntityToUser(userRepository.findById(id).orElse(null)));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        return mapEntityToUser(userRepository.save(mapUserToEntity(user)));
    }

    private User mapEntityToUser(UserEntity userEntity) {
        if (userEntity == null)
            return null;
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .active(userEntity.isActive())
                .build();
    }

    private UserEntity mapUserToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
