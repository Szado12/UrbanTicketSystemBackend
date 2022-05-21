package com.piisw.UrbanTicketSystem.domain.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    //Tickets
    private List<Ticket> tickets;
}
