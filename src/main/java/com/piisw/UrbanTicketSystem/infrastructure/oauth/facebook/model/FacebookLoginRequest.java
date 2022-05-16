package com.piisw.UrbanTicketSystem.infrastructure.oauth.facebook.model;

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
