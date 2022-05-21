package com.piisw.UrbanTicketSystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket{
    private long id;
    private LocalDateTime boughtTime;
    private LocalDateTime validatedTime;
    private String status;
    private TicketType type;
}
