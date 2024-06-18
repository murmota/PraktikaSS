package com.example.PraktikaSS.Configs;

import com.example.PraktikaSS.models.RememberMeToken;
import com.example.PraktikaSS.repositories.RememberMeTokenRepository;
import com.example.PraktikaSS.security.TokenFilter;
import com.example.PraktikaSS.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.time.ZoneId;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private RememberMeTokenRepository tokenRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new PersistentTokenRepository() {
            @Override
            public void createNewToken(PersistentRememberMeToken token) {
                RememberMeToken rememberMeToken = new RememberMeToken();
                rememberMeToken.setSeries(token.getSeries());
                rememberMeToken.setUsername(token.getUsername());
                rememberMeToken.setToken(token.getTokenValue());
                rememberMeToken.setLastUsed(token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                tokenRepository.save(rememberMeToken);
            }

            @Override
            public void updateToken(String series, String tokenValue, Date lastUsed) {
                tokenRepository.findBySeries(series).ifPresent(token -> {
                    token.setToken(tokenValue);
                    token.setLastUsed(lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    tokenRepository.save(token);
                });
            }

            @Override
            public PersistentRememberMeToken getTokenForSeries(String seriesId) {
                return tokenRepository.findBySeries(seriesId)
                        .map(token -> new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getToken(),
                                Date.from(token.getLastUsed().atZone(ZoneId.systemDefault()).toInstant())))
                        .orElse(null);
            }

            @Override
            public void removeUserTokens(String username) {
                tokenRepository.deleteByUsername(username);
            }
        };
    }
}
