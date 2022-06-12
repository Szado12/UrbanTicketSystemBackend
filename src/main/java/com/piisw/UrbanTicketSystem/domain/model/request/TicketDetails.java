package com.piisw.UrbanTicketSystem.domain.model.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketDetails {
    private String ticketUuid;
    private int validatedInBus;
}
