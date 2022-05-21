package com.piisw.UrbanTicketSystem.infrastructure.oauth.facebook.adapter;


import com.piisw.UrbanTicketSystem.domain.model.User;
import com.piisw.UrbanTicketSystem.domain.model.UserRole;
import com.piisw.UrbanTicketSystem.domain.model.security.ApplicationUserDetails;
import com.piisw.UrbanTicketSystem.domain.port.OAuthRepository;
import com.piisw.UrbanTicketSystem.domain.port.SecurityRepository;
import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jwt.JwtTokenProvider;
import com.piisw.UrbanTicketSystem.infrastructure.oauth.facebook.client.FacebookClient;
import com.piisw.UrbanTicketSystem.domain.model.security.FacebookLoginRequest;
import com.piisw.UrbanTicketSystem.infrastructure.oauth.facebook.model.FacebookUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class FacebookService implements OAuthRepository {
    @Autowired
    private FacebookClient facebookClient;
    @Autowired
    private SecurityRepository securityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public String authorizeUser(FacebookLoginRequest facebookLoginRequest) {
        boolean firstRegistration = userFirstRegistration(facebookLoginRequest.getAccessToken());
        String token = loginUser(facebookLoginRequest.getAccessToken());
        String name = getName(facebookLoginRequest.getAccessToken());
        String surname = getSurname(facebookLoginRequest.getAccessToken());
        return String.format("{ \"token\": \"%s\", \"registration\": \"%s\", \"name\": \"%s\", \"surname\": \"%s\"}", "Bearer "+token, firstRegistration ? 1 : 0, name, surname);
    }

    private String loginUser(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);

        return userRepository.findByUsername(facebookUser.getEmail())
                .or(() -> Optional.ofNullable(securityRepository.registerUser(convertTo(facebookUser), UserRole.OAUTH_CLIENT)))
                .map(ApplicationUserDetails::new)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(tokenProvider::generateToken)
                .orElseThrow(() ->
                        new RuntimeException("unable to login facebook user id " + facebookUser.getId()));
    }

    public boolean userFirstRegistration(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        Optional<User> userOptional = userRepository.findByUsername(facebookUser.getEmail());
        if (userOptional.isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    public String getName(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        return userRepository.findByUsername(facebookUser.getEmail()).get().getName();
    }

    public String getSurname(String fbAccessToken) {
        var facebookUser = facebookClient.getUser(fbAccessToken);
        return userRepository.findByUsername(facebookUser.getEmail()).get().getSurname();
    }

    private User convertTo(FacebookUser facebookUser) {
        return User.builder()
                .id((long) facebookUser.getId().hashCode())
                .username(facebookUser.getEmail())
                .password(generatePassword(8))
                .name(facebookUser.getFirstName())
                .surname(facebookUser.getLastName())
                .build();
    }

    private String generateUsername(String firstName, String lastName) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%s.%s.%06d", firstName, lastName, number);
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }

}
