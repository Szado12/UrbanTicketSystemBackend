package com.piisw.UrbanTicketSystem.domain.model.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketValidityResponse {
    private boolean valid;
    private boolean reduced;
}
