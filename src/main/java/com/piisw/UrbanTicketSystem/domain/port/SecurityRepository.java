package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;

public interface SecurityRepository {
    String loginUser(String username, String password);
    User registerUser(User user, UserRole role);
}
