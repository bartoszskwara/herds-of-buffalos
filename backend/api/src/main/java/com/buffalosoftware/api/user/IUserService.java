package com.buffalosoftware.api.user;

import com.buffalosoftware.dto.user.UserDataDto;
import com.buffalosoftware.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    UserDataDto getUserData(Long userId);
}
