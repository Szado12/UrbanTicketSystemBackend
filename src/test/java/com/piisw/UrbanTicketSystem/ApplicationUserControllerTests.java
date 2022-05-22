package com.piisw.UrbanTicketSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piisw.UrbanTicketSystem.domain.model.Ticket;
import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.infrastructure.jpa.adapter.JpaUserService;
import com.piisw.UrbanTicketSystem.infrastructure.jwt.JwtConfig;
import com.piisw.UrbanTicketSystem.infrastructure.jwt.JwtSecretConfig;
import com.piisw.UrbanTicketSystem.infrastructure.oauth.facebook.adapter.FacebookService;
import com.piisw.UrbanTicketSystem.infrastructure.security.adapter.ApplicationUserService;
import com.piisw.UrbanTicketSystem.ui.rest.controller.ApplicationUserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ApplicationUserController.class)
@Import(ApplicationUserController.class)
@ContextConfiguration(classes = {JwtSecretConfig.class, JwtConfig.class})
public class ApplicationUserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserService userService;

    @MockBean
    private ApplicationUserService securityService;

    @MockBean
    private FacebookService facebookService;

    ObjectMapper mapper = new ObjectMapper();

    @WithMockUser(value = "klient@gmail.com")
    @Test
    public void getProfileShouldReturnUser() throws Exception {
        User testUser = new User(0L, "user@gmail.com", "1234", "CLIENT", true, "Jan", "Kowalski", new ArrayList<Ticket>());

        when(userService.findById(0L)).thenReturn(Optional.of(testUser));
        mockMvc.perform(get("/profile").requestAttr("id", 0L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.surname").value(testUser.getSurname()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.role").value(testUser.getRole()))
                .andExpect(jsonPath("$.active").value(testUser.isActive()));
    }


    @WithMockUser(value = "user@gmail.com")
    @Test
    public void registerShouldCreateUser() throws Exception {
        User testUser = new User(0L, "user@gmail.com", "1234", "CLIENT", true, "Jan", "Kowalski", new ArrayList<Ticket>());

        when(userService.save(testUser)).thenReturn(testUser);
        when(securityService.registerUser(testUser, UserRole.CLIENT)).thenReturn(testUser);
        mockMvc.perform(post("/register").with(csrf()).content(mapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                        .andExpect(jsonPath("$.name").value(testUser.getName()))
                        .andExpect(jsonPath("$.surname").value(testUser.getSurname()))
                        .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                        .andExpect(jsonPath("$.role").value(testUser.getRole()))
                        .andExpect(jsonPath("$.active").value(testUser.isActive()));
    }
}
