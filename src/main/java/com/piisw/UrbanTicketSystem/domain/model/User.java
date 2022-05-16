package com.piisw.UrbanTicketSystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    //Account fields
    private long id;
    private String username;
    private String password;
    private String role;
    private boolean active;

    //Personal data fields
    private String name;
    private String surname;
}
