package com.piisw.UrbanTicketSystem.infrastructure.jpa.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
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
    private String email;
    private String password;
    private String role;
    private boolean active;

    //Personal data fields
    private String name;
    private String surname;
}
