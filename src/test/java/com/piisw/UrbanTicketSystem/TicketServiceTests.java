package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketCategory;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.request.TicketValidityResponse;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketTypeService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketCategoryEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.TicketTypeEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaTicketRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import org.apache.tomcat.jni.Local;
import org.hibernate.mapping.Any;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.INVALID;
import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.VALID;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaTicketService.class})
public class TicketServiceTests {
    @InjectMocks
    JpaTicketService jpaTicketService;

    @Mock
    JpaTicketRepository jpaTicketRepository;

    private Ticket testTicket(LocalDateTime time) {
        Ticket testTicket = new Ticket();
        testTicket.setId(0L);
        testTicket.setStatus("BOUGHT");
        testTicket.setValidatedInBus(0);
        testTicket.setUuid("UUID");
        testTicket.setBoughtTime(time);
        testTicket.setValidatedTime(time);
        testTicket.setType(new TicketType(0L, 123, true, new TicketCategory(0L, "TIME_TICKET"),60, 0, "NAME"));
        return testTicket;
    }

    private TicketEntity testTicketEntity(LocalDateTime time) {
        TicketEntity testTicketEntity = new TicketEntity();
        testTicketEntity.setId(0L);
        testTicketEntity.setTicketStatus("BOUGHT");
        testTicketEntity.setValidatedInBus(0);
        testTicketEntity.setUuid("UUID");
        testTicketEntity.setBoughtTime(time);
        testTicketEntity.setValidatedTime(time);
        testTicketEntity.setType(new TicketTypeEntity(0L, 123, true, "NAME", new TicketCategoryEntity(0L, "TIME_TICKET"),60, 0));
        return testTicketEntity;
    }

    @Test
    public void shouldReturnTicketOnSave() {
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = testTicket(time);
        TicketEntity testTicketEntity = testTicketEntity(time);

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
        String uuid = "UUID";
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = testTicket(time);
        TicketEntity testTicketEntity = testTicketEntity(time);

        when(jpaTicketRepository.findByUuid(uuid)).thenReturn(Optional.of(testTicketEntity));
        when(jpaTicketRepository.save(any(TicketEntity.class))).thenReturn(testTicketEntity);
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
        Ticket testTicket = testTicket(time);
        TicketEntity testTicketEntity = testTicketEntity(time);

        when(jpaTicketRepository.findById(0L)).thenReturn(Optional.of(testTicketEntity));
        when(jpaTicketRepository.save(any(TicketEntity.class))).thenReturn(testTicketEntity);
        Ticket ticket = jpaTicketService.findById(0L);
        assertThat(ticket.getId()).isSameAs(testTicket.getId());
        assertThat(ticket.getUuid()).isSameAs(testTicket.getUuid());
        assertThat(ticket.getStatus()).isSameAs(testTicket.getStatus());
        assertThat(ticket.getValidatedInBus()).isSameAs(testTicket.getValidatedInBus());
        assertThat(ticket.getValidatedTime()).isSameAs(testTicket.getValidatedTime());
        assertThat(ticket.getBoughtTime()).isSameAs(testTicket.getBoughtTime());
    }

    @Test
    public void shouldValidateTicket() {
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = testTicket(time);
        TicketEntity testTicketEntity = testTicketEntity(time);

        when(jpaTicketRepository.findByUuid(any(String.class))).thenReturn(Optional.of(testTicketEntity));
        when(jpaTicketRepository.save(any(TicketEntity.class))).then(returnsFirstArg());

        Ticket validatedTicket = jpaTicketService.validateTicket("UUID", 123).get();
        assertThat(validatedTicket.getStatus()).isSameAs(VALID.name());
        assertThat(validatedTicket.getValidatedInBus()).isSameAs(123);
    }

    @Test
    public void shouldUpdateTicketValidity() {
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = testTicket(time);
        testTicket.setStatus(VALID.name());
        testTicket.setValidatedTime(time.minusHours(2));

        when(jpaTicketRepository.save(any(TicketEntity.class))).then(returnsFirstArg());
        Ticket updatedTicket = jpaTicketService.updateValidity(testTicket);
        assertThat(updatedTicket.getStatus()).isSameAs(INVALID.name());
    }

    @Test
    public void shouldReturnTicketValidity() {
        LocalDateTime time = LocalDateTime.now();
        Ticket testTicket = testTicket(time);
        testTicket.setStatus(VALID.name());
        TicketEntity testTicketEntity = testTicketEntity(time);
        testTicketEntity.setTicketStatus(VALID.name());

        when(jpaTicketRepository.findByUuid(any(String.class))).thenReturn(Optional.of(testTicketEntity));
        when(jpaTicketRepository.save(any(TicketEntity.class))).then(returnsFirstArg());
        TicketValidityResponse ticketValidityResponse = jpaTicketService.checkTicketValidity("UUID", 123);
        assert(ticketValidityResponse.isValid());
        assert(ticketValidityResponse.isReduced());
    }
}
