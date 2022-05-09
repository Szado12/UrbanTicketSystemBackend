package com.piisw.UrbanTicketSystem.controller;


import com.piisw.UrbanTicketSystem.authentication.ApplicationUserService;
import com.piisw.UrbanTicketSystem.jwt.JwtTokenProvider;
import com.piisw.UrbanTicketSystem.model.Client;
import com.piisw.UrbanTicketSystem.model.User;
import com.piisw.UrbanTicketSystem.repository.ClientRepository;
import com.piisw.UrbanTicketSystem.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ApplicationUserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserController(UserRepository userRepository, PasswordEncoder passwordEncoder, ClientRepository clientRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, ApplicationUserService applicationUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.applicationUserService = applicationUserService;
    }

    //Ciekawa sprawa, gdy ma się w bazie już usera o danym ID to i tak probuje za pierwszym razem utworzyc i wyrzuca
    //wyjatek, dopiero za drugim razem podnosi auto generowany indeks
    @PostMapping("/register")
    ResponseEntity<Object> register(@RequestBody Client newClient) {
        Optional<User> checkUsername = userRepository.findByUsername(newClient.getUsername());
        if (checkUsername.isPresent())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        else {
            String unHashedPassword= newClient.getPassword();
            newClient.setPassword(passwordEncoder.encode(unHashedPassword));
            newClient.setActive(true);
            newClient.setRole("CLIENT");
            return new ResponseEntity<>(clientRepository.save(newClient), HttpStatus.CREATED);
        }
    }

    @GetMapping("/userrole")
    public  ResponseEntity<?> getUserRole(@RequestAttribute Long id) {
        Client client=clientRepository.findById(id).get();
        String role=client.getRole();
        Map<String, String> jsonMap=new HashMap<>();
        jsonMap.put("role", role);
        JSONObject jsonObject = new JSONObject(jsonMap);
        return ResponseEntity.ok(new JSONObject(jsonMap));
    }
    

}
