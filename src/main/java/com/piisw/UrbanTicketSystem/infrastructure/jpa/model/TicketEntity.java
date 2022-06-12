package com.piisw.UrbanTicketSystem.infrastructure.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.piisw.UrbanTicketSystem.domain.model.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String uuid;
    @DateTimeFormat
    private LocalDateTime boughtTime;
    @DateTimeFormat
    private LocalDateTime validatedTime;
    private int validatedInBus;
    private String status;
    @ManyToOne
    private TicketTypeEntity type;
}
