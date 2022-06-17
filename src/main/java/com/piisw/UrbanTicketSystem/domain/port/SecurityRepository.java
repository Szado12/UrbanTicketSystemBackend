package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;

public interface SecurityRepository {
    User registerUser(User user, UserRole role);
    User updateUserCredentials(Long id, User updatedUser);
}
