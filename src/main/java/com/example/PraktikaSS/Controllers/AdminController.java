package com.example.PraktikaSS.Controllers;

import com.example.PraktikaSS.dal.DataAccessLayer;
import com.example.PraktikaSS.models.Notes;
import com.example.PraktikaSS.security.JwtCore;
import com.example.PraktikaSS.service.UserDetailsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/admin")
public class AdminController {
    private final DataAccessLayer dataAccessLayer;
    private final UserDetailsServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserDetailsServiceImpl userService, DataAccessLayer dataAccessLayer) {
        this.userService = userService;
        this.dataAccessLayer = dataAccessLayer;
    }
    @Autowired
    private JwtCore jwtCore;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create/notes")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createNotes(@RequestBody Notes notes) {
        dataAccessLayer.createNotes(notes);
        return ResponseEntity.ok("Notes added successfully!");
    }
}