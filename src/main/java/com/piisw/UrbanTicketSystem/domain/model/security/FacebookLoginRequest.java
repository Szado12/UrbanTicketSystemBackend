package com.piisw.UrbanTicketSystem.domain.model.security;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FacebookLoginRequest implements Serializable {
    private String accessToken;

    public String getAccessToken() { return accessToken; }
}
