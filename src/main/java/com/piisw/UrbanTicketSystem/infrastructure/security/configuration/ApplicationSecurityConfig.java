package com.piisw.UrbanTicketSystem.infrastructure.security.configuration;

import com.piisw.UrbanTicketSystem.domain.port.UserRepository;
import com.piisw.UrbanTicketSystem.infrastructure.jwt.JwtConfig;
import com.piisw.UrbanTicketSystem.infrastructure.security.jwt.JwtTokenVerifier;
import com.piisw.UrbanTicketSystem.infrastructure.security.jwt.JwtUsernamePasswordAuthFilter;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.AlgorithmParametersSpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;

import static com.piisw.UrbanTicketSystem.domain.model.UserPermission.CLIENT_READ;
import static com.piisw.UrbanTicketSystem.domain.model.UserRole.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(@Lazy UserDetailsService userDetailsService, @Lazy UserRepository userRepository, SecretKey secretKey, JwtConfig jwtConfig) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().and()
                .addFilter(new JwtUsernamePasswordAuthFilter(authenticationManager(), jwtConfig, secretKey, userRepository))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey, userRepository), JwtUsernamePasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/home", "/healthcheck", "/register", "/swagger-ui.html", "/login", "/facebook/login").permitAll()
                .antMatchers("/profile", "/userPanel", "/profile/data", "/profile/password").hasAnyRole(CLIENT.name(), OAUTH_CLIENT.name(), STAFF.name())
                .antMatchers(HttpMethod.POST, "/ticket", "tickets").hasAnyRole(CLIENT.name(), OAUTH_CLIENT.name())
                .antMatchers(HttpMethod.PUT, "/ticket/validate").permitAll()
                .antMatchers("/workerPanel").hasRole(STAFF.name())
                .antMatchers(HttpMethod.GET,"/ticket", "/tickettypes").hasAnyRole(CLIENT.name(), OAUTH_CLIENT.name(), STAFF.name())
                .antMatchers("/adminPanel").hasRole(ADMIN.name())
                .antMatchers(HttpMethod.POST,"/tickettypes").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
