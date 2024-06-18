package com.example.PraktikaSS.service;

import com.example.PraktikaSS.dal.DataAccessLayer;
import com.example.PraktikaSS.dto.SignupRequest;
import com.example.PraktikaSS.models.User;
import com.example.PraktikaSS.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final DataAccessLayer dataAccessLayer;
    @Autowired
    public UserDetailsServiceImpl(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }
    public String newUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setUserSurName(signupRequest.getUserSurName());
        user.setUserMiddleName(signupRequest.getUserMiddleName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setRoles(signupRequest.getRoles());

        return dataAccessLayer.newUserToDatabase(user);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = dataAccessLayer.getUserFromDatabaseByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return UserDetailsImpl.build(user);
    }

    public User loadUserEntityByEmail(String email) throws UsernameNotFoundException {
        User user = dataAccessLayer.getUserFromDatabaseByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;
    }

}
