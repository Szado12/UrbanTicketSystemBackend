package com.piisw.UrbanTicketSystem.infrastructure.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Account fields
    private String username;
    private String password;
    private String role;
    private boolean active;

    //Personal data fields
    private String name;
    private String surname;

    //tickets
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<TicketEntity> tickets = new ArrayList<>();
}
