package com.piisw.UrbanTicketSystem.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketType {
    private long id;
    private long price;
    private boolean reduced;
    private String category;
    private int minutesOfValidity;
    private int daysOfValidity;
}
