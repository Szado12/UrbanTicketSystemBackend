package com.piisw.UrbanTicketSystem;

import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.TicketType;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaTicketService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.model.UserEntity;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.repository.JpaUserRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.piisw.UrbanTicketSystem.domain.model.TicketStatus.BOUGHT;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {JpaUserService.class})
public class UserServiceTests {
    @InjectMocks
    JpaUserService jpaUserService;

    @Mock
    JpaUserRepository jpaUserRepository;

    @Mock
    JpaTicketService jpaTicketService;

    private User testUser() {
        User testUser = new User();
        testUser.setId(0L);
        testUser.setUsername("user@gmail.com");
        testUser.setPassword("1234");
        testUser.setTickets(new ArrayList<>());
        return testUser;
    }

    private UserEntity testUserEntity() {
        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setId(0L);
        testUserEntity.setUsername("user@gmail.com");
        testUserEntity.setPassword("1234");
        testUserEntity.setTickets(new ArrayList<>());
        return testUserEntity;
    }

    private Ticket testTicket() {
        TicketType ticketType = new TicketType();
        ticketType.setId(0L);
        ticketType.setPrice(123);
        Ticket testTicket = new Ticket();
        testTicket.setUuid("abcdef");
        testTicket.setType(ticketType);
        testTicket.setValidatedTime(null);
        testTicket.setValidatedInBus(0);
        testTicket.setStatus(BOUGHT.name());
        return testTicket;
    }

    @Test
    public void shouldReturnUserOnSave() {
        User testUser = testUser();
        UserEntity testUserEntity = testUserEntity();
        when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(testUserEntity);
        User createdUser = jpaUserService.save(testUser);
        assertThat(createdUser.getUsername()).isSameAs(testUser.getUsername());
        assertThat(createdUser.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnUserByUsername() {
        String username = "user@gmail.com";
        User testUser = testUser();
        UserEntity testUserEntity = testUserEntity();
        when(jpaUserRepository.findByUsername(username)).thenReturn(Optional.of(testUserEntity));
        User user = jpaUserService.findByUsername(username).get();
        assertThat(user.getUsername()).isSameAs(testUser.getUsername());
        assertThat(user.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnUserById() {
        User testUser = testUser();
        UserEntity testUserEntity = testUserEntity();
        when(jpaUserRepository.findById(0L)).thenReturn(Optional.of(testUserEntity));
        User user = jpaUserService.findById(0L).get();
        assertThat(user.getUsername()).isSameAs(testUser.getUsername());
        assertThat(user.getPassword()).isSameAs(testUser.getPassword());
    }

    @Test
    public void shouldReturnIfExistsByUsername() {
        String username = "user@gmail.com";
        User testUser = testUser();
        when(jpaUserRepository.existsByUsername(username)).thenReturn(true);
        assert (jpaUserService.existsByUsername(username));
    }

    @Test
    public void shouldUpdateUserData() {
        UserEntity testUserEntity = testUserEntity();
        User testUserUpdated = testUser();
        testUserUpdated.setName("A");
        testUserUpdated.setSurname("B");
        when(jpaUserRepository.findById(0L)).thenReturn(Optional.of(testUserEntity));
        when(jpaUserRepository.save(any(UserEntity.class))).then(returnsFirstArg());
        User updatedUser = jpaUserService.updateUserData(0L, testUserUpdated);
        assertThat(updatedUser.getName()).isSameAs(testUserUpdated.getName());
        assertThat(updatedUser.getSurname()).isSameAs(testUserUpdated.getSurname());
    }

    @Test
    public void shoouldAddTicketToUser() {
        Long id = 0L;
        UserEntity testUserEntity = testUserEntity();
        Ticket testTicket = testTicket();

        when(jpaTicketService.buyTicket(id)).thenReturn(testTicket);
        when(jpaUserRepository.save(any(UserEntity.class))).then(returnsFirstArg());
        when(jpaUserRepository.findById(id)).thenReturn(Optional.of(testUserEntity));
        Ticket boughtTicket = jpaUserService.addTicket(id,id);
        assertThat(boughtTicket.getId()).isSameAs(testTicket.getId());
        assertThat(boughtTicket.getUuid()).isSameAs(testTicket.getUuid());
        assertThat(boughtTicket.getStatus()).isSameAs(testTicket.getStatus());
        assertThat(boughtTicket.getValidatedInBus()).isSameAs(testTicket.getValidatedInBus());
        assertThat(boughtTicket.getValidatedTime()).isSameAs(testTicket.getValidatedTime());
        assertThat(boughtTicket.getType().getPrice()).isSameAs(testTicket.getType().getPrice());
        assertThat(boughtTicket.getType().getId()).isSameAs(testTicket.getType().getId());
        assertThat(boughtTicket.getType().getDisplayName()).isSameAs(testTicket.getType().getDisplayName());
        assertThat(boughtTicket.getType().getMinutesOfValidity()).isSameAs(testTicket.getType().getMinutesOfValidity());
        assertThat(boughtTicket.getType().getDaysOfValidity()).isSameAs(testTicket.getType().getDaysOfValidity());
    }

    @Test
    public void shouldAddTicketsToUser() {
        List<Long> ticketTypeIds = Arrays.asList(1L, 2L, 3L);
        List<Long> ticketTypeCounts = Arrays.asList(1L, 1L, 1L);
        UserEntity testUserEntity = testUserEntity();
        Ticket testTicket1 = testTicket();
        testTicket1.setId(1L);
        Ticket testTicket2 = testTicket();
        testTicket1.setId(2L);
        Ticket testTicket3 = testTicket();
        testTicket1.setId(3L);
        List<Ticket> testList=new ArrayList<>();
        testList.add(testTicket1); testList.add(testTicket2); testList.add(testTicket3);
        when(jpaTicketService.buyTicket(1L)).thenReturn(testTicket1);
        when(jpaTicketService.buyTicket(2L)).thenReturn(testTicket2);
        when(jpaTicketService.buyTicket(3L)).thenReturn(testTicket3);
        when(jpaUserRepository.save(any(UserEntity.class))).then(returnsFirstArg());
        when(jpaUserRepository.findById(0L)).thenReturn(Optional.of(testUserEntity));
        List<Ticket> ticketList = jpaUserService.addTickets(0L, ticketTypeIds, ticketTypeCounts);
        assertThat(testList.size()).isSameAs(ticketList.size());
        assertThat(testList.get(0).getId()).isSameAs(testTicket1.getId());
        assertThat(testList.get(1).getId()).isSameAs(testTicket2.getId());
        assertThat(testList.get(2).getId()).isSameAs(testTicket3.getId());
    }
}
