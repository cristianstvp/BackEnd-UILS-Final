package com.example.uilsback.service.interfaces;

import com.example.uilsback.dto.EditUserRequestDTO;
import com.example.uilsback.dto.UserDataDTO;
import com.example.uilsback.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    public UserDetailsService userDetailsService();

    public List<User> findAll();

    public User save(User newUser);

    public void delete(Long id);

    public boolean edit(EditUserRequestDTO editedUser, String userId);

    public UserDataDTO getUserData();

    public boolean checkUser(String username);

}
