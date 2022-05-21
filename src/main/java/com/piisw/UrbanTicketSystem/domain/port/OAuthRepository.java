package com.piisw.UrbanTicketSystem.domain.port;

import com.piisw.UrbanTicketSystem.domain.model.security.FacebookLoginRequest;

public interface OAuthRepository {
    public String authorizeUser(FacebookLoginRequest request);
}
