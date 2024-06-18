package com.example.PraktikaSS.Controllers;

import com.example.PraktikaSS.PraktikaSSApplication;
import com.example.PraktikaSS.dto.SigninRequest;
import com.example.PraktikaSS.dto.SignupRequest;
import com.example.PraktikaSS.exception.UnauthorizedException;
import com.example.PraktikaSS.security.JwtCore;
import com.example.PraktikaSS.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;


@RestController
@Slf4j
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class SecurityController {
    private final UserDetailsServiceImpl userService;
    private final JwtCore jwtCore;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityController(UserDetailsServiceImpl userService, JwtCore jwtCore, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtCore = jwtCore;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        signupRequest.setRoles(Set.of("ROLE_ADMIN"));
        String serviceResult = userService.newUser(signupRequest);
        if (Objects.equals(serviceResult, "Выберите другую почту")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы. Теперь можете войти в свой аккаунт.");
    }
    @PostMapping("/signin")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        UserDetails user = userService.loadUserByUsername(signinRequest.getEmail());
        if (user == null || !passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            log.info("Ошибка авторизации пользователя " + signinRequest.getEmail());
            throw new UnauthorizedException("Ошибка авторизации пользователя " + signinRequest.getEmail());
        }
        String jwt = jwtCore.generateToken(user);
        PraktikaSSApplication.currentUser = userService.loadUserEntityByEmail(signinRequest.getEmail());
        log.info("Вход прошёл успешно");
        return ResponseEntity.ok(jwt);
    }


}
