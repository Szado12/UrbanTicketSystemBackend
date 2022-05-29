package com.piisw.UrbanTicketSystem.domain.model.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketsRequest {
    private List<Long> ticketTypeIds;
    private List<Long> ticketTypeCounts;
}
