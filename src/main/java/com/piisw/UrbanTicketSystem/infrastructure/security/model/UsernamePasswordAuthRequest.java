package com.piisw.UrbanTicketSystem.infrastructure.security.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UsernamePasswordAuthRequest {
    private String username;
    private String password;
}
