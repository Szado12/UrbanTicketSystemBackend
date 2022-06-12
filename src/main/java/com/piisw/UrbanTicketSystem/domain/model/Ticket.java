package com.piisw.UrbanTicketSystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket{
    private long id;
    private String uuid;
    private LocalDateTime boughtTime;
    private LocalDateTime validatedTime;
    private int validatedInBus;
    private String status;
    private TicketType type;
}
