package com.buffalosoftware.api.user;

import com.buffalosoftware.dto.user.CreateUserRequestDto;
import com.buffalosoftware.dto.user.UserDto;
import com.buffalosoftware.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    UserDto findRandomUserWithMultipleCities();
    UserDto findUserById(Long userId);
    void createUser(CreateUserRequestDto createUserRequestDto);
}
