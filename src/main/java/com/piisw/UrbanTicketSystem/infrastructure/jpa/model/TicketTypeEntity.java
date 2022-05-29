package com.piisw.UrbanTicketSystem.infrastructure.jpa.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ticket_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long price;
    private boolean reduced;
    private String displayName;
    @ManyToOne
    private TicketCategoryEntity category;
    private int minutesOfValidity;
    private int daysOfValidity;
}
