package com.example.PraktikaSS.Controllers;
import com.example.PraktikaSS.dal.DataAccessLayer;
import com.example.PraktikaSS.dto.SignupRequest;
import com.example.PraktikaSS.models.*;
import com.example.PraktikaSS.security.JwtCore;
import com.example.PraktikaSS.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/admin")
public class AdminController {
    private final DataAccessLayer dataAccessLayer;
    private final UserDetailsServiceImpl userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    public void someMethod() {
        logger.debug("Debug message");
//        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message");
    }
    @Autowired
    public AdminController(UserDetailsServiceImpl userService, DataAccessLayer dataAccessLayer) {
        this.userService = userService;
        this.dataAccessLayer = dataAccessLayer;
    }
    @Autowired
    private JwtCore jwtCore;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        signupRequest.setRoles(Set.of("ROLE_ADMIN"));
        String serviceResult = userService.newUser(signupRequest);
        if (Objects.equals(serviceResult, "Выберите другое имя")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        if (Objects.equals(serviceResult, "Выберите другую почту")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serviceResult);
        }
        return ResponseEntity.ok("Администратор успешно создан.");
    }
    @PostMapping("/create/product")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        try {
            dataAccessLayer.createProduct(product);
            return ResponseEntity.ok("Product added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product: " + e.getMessage());
        }
    }
    @GetMapping("/get/orders")
    public ResponseEntity getOrders(){
        return ResponseEntity.ok(dataAccessLayer.getOrders());
    }
    @GetMapping("/get/users")
    public ResponseEntity getUsers(){
        return ResponseEntity.ok(dataAccessLayer.getUsers());
    }
    @GetMapping("/get/baskets")
    public ResponseEntity getBaskets(){
        return ResponseEntity.ok(dataAccessLayer.getBaskets());
    }
    @DeleteMapping("/delete/product/{id}")
    public ResponseEntity deleteProductById(@PathVariable("id") long id){
        dataAccessLayer.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }
    @PutMapping("/update/product/{id}")
    public ResponseEntity updateProductById(@PathVariable("id") long id, @RequestBody Product updatedProduct){
        dataAccessLayer.updateProductById(id, updatedProduct);
        return ResponseEntity.ok("Product updated successfully!");
    }
    @PostMapping("/create/user")
    public ResponseEntity createUser(@RequestBody User user){
        dataAccessLayer.createUser(user);
        return ResponseEntity.ok("User added successfully!");
    }
    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable("id") long id){
        dataAccessLayer.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully!");
    }
    @PostMapping("/create/discount")
    public ResponseEntity createDiscount(@RequestBody Discount discount){
        dataAccessLayer.createDiscount(discount);
        return ResponseEntity.ok("Discount added successfully!");
    }
    @DeleteMapping("/delete/discount/{id}")
    public ResponseEntity deleteDiscountById(@PathVariable("id") long id){
        dataAccessLayer.deleteDiscountById(id);
        return ResponseEntity.ok("Discount deleted successfully!");
    }
    @PutMapping("/update/discount/{id}")
    public ResponseEntity updateDiscountById(@PathVariable("id") long id, @RequestBody Discount updatedDiscount){
        dataAccessLayer.updateDiscount(id, updatedDiscount);
        return ResponseEntity.ok("Discount updated successfully!");
    }
    @PostMapping("/create/point")
    public ResponseEntity createPoint(@RequestBody Point point){
        dataAccessLayer.createPoint(point);
        return ResponseEntity.ok("Point added successfully!");
    }
    @DeleteMapping("/delete/point/{id}")
    public ResponseEntity deletePointById(@PathVariable("id") long id){
        dataAccessLayer.deletePointById(id);
        return ResponseEntity.ok("Point deleted successfully!");
    }
    @PutMapping("/update/point/{id}")
    public ResponseEntity updatePointById(@PathVariable("id") long id, @RequestBody Point updatedPoint){
        dataAccessLayer.updatePoint(id, updatedPoint);
        return ResponseEntity.ok("Point updated successfully!");
    }

}