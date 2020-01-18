package com.buffalosoftware.api.user;

import com.buffalosoftware.dto.building.UserDto;
import com.buffalosoftware.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    UserDto findRandomUserWithMultipleCities();
    UserDto findUserById(Long userId);
}
