package com.piisw.UrbanTicketSystem.infrastructure.jpa.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ticket_categories")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
}
