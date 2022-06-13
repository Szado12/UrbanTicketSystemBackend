package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketTypeService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaTicketService.class})
public class TicketServiceTests {
    @InjectMocks
    JpaTicketService jpaTicketService;

    @Mock
    JpaTicketRepository jpaTicketRepository;

    @Test
    public void shouldReturnTicketOnSave() {
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = new Ticket();
        testTicket.setId(0L);
        testTicket.setStatus("BOUGHT");
        testTicket.setValidatedInBus(0);
        testTicket.setUuid("UUID");
        testTicket.setBoughtTime(time);
        testTicket.setValidatedTime(time);

        TicketEntity testTicketEntity = new TicketEntity();
        testTicketEntity.setId(0L);
        testTicketEntity.setTicketStatus("BOUGHT");
        testTicketEntity.setValidatedInBus(0);
        testTicketEntity.setUuid("UUID");
        testTicketEntity.setBoughtTime(time);
        testTicketEntity.setValidatedTime(time);

        when(jpaTicketRepository.save(any(TicketEntity.class))).thenReturn(testTicketEntity);
        Ticket ticket = jpaTicketService.save(testTicket);
        assertThat(ticket.getId()).isSameAs(testTicket.getId());
        assertThat(ticket.getUuid()).isSameAs(testTicket.getUuid());
        assertThat(ticket.getStatus()).isSameAs(testTicket.getStatus());
        assertThat(ticket.getValidatedInBus()).isSameAs(testTicket.getValidatedInBus());
        assertThat(ticket.getValidatedTime()).isSameAs(testTicket.getValidatedTime());
        assertThat(ticket.getBoughtTime()).isSameAs(testTicket.getBoughtTime());
    }

    @Test
    public void shouldReturnTicketByUuid() {
        LocalDateTime time = LocalDateTime.now();
        String uuid = "UUID";
        Ticket testTicket = new Ticket();
        testTicket.setId(0L);
        testTicket.setStatus("BOUGHT");
        testTicket.setValidatedInBus(0);
        testTicket.setUuid(uuid);
        testTicket.setBoughtTime(time);
        testTicket.setValidatedTime(time);

        TicketEntity testTicketEntity = new TicketEntity();
        testTicketEntity.setId(0L);
        testTicketEntity.setTicketStatus("BOUGHT");
        testTicketEntity.setValidatedInBus(0);
        testTicketEntity.setUuid(uuid);
        testTicketEntity.setBoughtTime(time);
        testTicketEntity.setValidatedTime(time);

        when(jpaTicketRepository.findByUuid(uuid)).thenReturn(Optional.of(testTicketEntity));
        Ticket ticket = jpaTicketService.findByUuid(uuid);
        assertThat(ticket.getId()).isSameAs(testTicket.getId());
        assertThat(ticket.getUuid()).isSameAs(testTicket.getUuid());
        assertThat(ticket.getStatus()).isSameAs(testTicket.getStatus());
        assertThat(ticket.getValidatedInBus()).isSameAs(testTicket.getValidatedInBus());
        assertThat(ticket.getValidatedTime()).isSameAs(testTicket.getValidatedTime());
        assertThat(ticket.getBoughtTime()).isSameAs(testTicket.getBoughtTime());
    }

    @Test
    public void shouldReturnTicketById() {
        LocalDateTime time = LocalDateTime.now();
        String uuid = "UUID";
        Ticket testTicket = new Ticket();
        testTicket.setId(0L);
        testTicket.setStatus("BOUGHT");
        testTicket.setValidatedInBus(0);
        testTicket.setUuid(uuid);
        testTicket.setBoughtTime(time);
        testTicket.setValidatedTime(time);

        TicketEntity testTicketEntity = new TicketEntity();
        testTicketEntity.setId(0L);
        testTicketEntity.setTicketStatus("BOUGHT");
        testTicketEntity.setValidatedInBus(0);
        testTicketEntity.setUuid(uuid);
        testTicketEntity.setBoughtTime(time);
        testTicketEntity.setValidatedTime(time);

        when(jpaTicketRepository.findById(0L)).thenReturn(Optional.of(testTicketEntity));
        Ticket ticket = jpaTicketService.findById(0L);
        assertThat(ticket.getId()).isSameAs(testTicket.getId());
        assertThat(ticket.getUuid()).isSameAs(testTicket.getUuid());
        assertThat(ticket.getStatus()).isSameAs(testTicket.getStatus());
        assertThat(ticket.getValidatedInBus()).isSameAs(testTicket.getValidatedInBus());
        assertThat(ticket.getValidatedTime()).isSameAs(testTicket.getValidatedTime());
        assertThat(ticket.getBoughtTime()).isSameAs(testTicket.getBoughtTime());
    }
}
