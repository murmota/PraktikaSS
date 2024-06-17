package com.example.PraktikaSS;

import com.example.PraktikaSS.dal.DataAccessLayer;
import com.example.PraktikaSS.dto.SigninRequest;
import com.example.PraktikaSS.dto.SignupRequest;
import com.example.PraktikaSS.models.User;
import com.example.PraktikaSS.security.JwtCore;
import com.example.PraktikaSS.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userService;

    @MockBean
    private DataAccessLayer dataAccessLayer;

    @MockBean
    private JwtCore jwtCore;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testSignupSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName("testuser");
        signupRequest.setPassword("password");

        when(userService.newUser(any(SignupRequest.class))).thenReturn("Success");

        mockMvc.perform(post("/secured/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Вы успешно зарегистрированы. Теперь можете войти в свой аккаунт."));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testSignupDuplicateUsername() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUserName("testuser");
        signupRequest.setPassword("password");

        when(userService.newUser(any(SignupRequest.class))).thenReturn("Выберите другое имя");

        mockMvc.perform(post("/secured/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Выберите другое имя"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testSigninSuccess() throws Exception {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUserName("testuser");
        signinRequest.setPassword("password");

        UserDetails user = mock(UserDetails.class);
        when(user.getPassword()).thenReturn("encodedPassword");

        when(userService.loadUserByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtCore.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/secured/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testSigninFailure() throws Exception {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUserName("testuser");
        signinRequest.setPassword("password");

        when(userService.loadUserByUsername(anyString())).thenReturn(null);

        mockMvc.perform(post("/secured/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteBasketsByUserId() throws Exception {
        mockMvc.perform(delete("/secured/delete/baskets/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("baskets"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateReview() throws Exception {
        mockMvc.perform(post("/secured/create/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Great product!\",\"rating\":5}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review added successfully!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteReviewById() throws Exception {
        mockMvc.perform(delete("/secured/delete/review/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review deleted successfully!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateOrder() throws Exception {
        mockMvc.perform(post("/secured/create/order/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteOrderById() throws Exception {
        mockMvc.perform(delete("/secured/delete/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testCreateBasket() throws Exception {
        mockMvc.perform(post("/secured/create/basket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Basket1\",\"items\":[]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Basket added successfully!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUserName("testuser");

        when(dataAccessLayer.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/secured/get/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetBasketsByUserId() throws Exception {
        mockMvc.perform(get("/secured/get/baskets/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testUpdateUserById() throws Exception {
        mockMvc.perform(put("/secured/update/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"updatedUser\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully!"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testGetOrderById() throws Exception {
        mockMvc.perform(get("/secured/get/order/1"))
                .andExpect(status().isOk());
    }
}
