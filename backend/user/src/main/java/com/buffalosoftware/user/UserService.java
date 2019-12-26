package com.buffalosoftware.user;

import com.buffalosoftware.api.user.IUserService;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.dto.user.UserDataDto;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.entity.UserResources;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDataDto getUserData(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));
        UserResources userResources = user.getUserResources();
        return UserDataDto.builder()
                .id(user.getId())
                .name(user.getName())
                .points(user.getPoints())
                .ranking(findRankByPoints(user))
                .numberOfCities(null)//TODO
                .resources(ResourcesDto.builder()
                        .wood(userResources != null ? userResources.getWood(): null)
                        .clay(userResources != null ? userResources.getClay(): null)
                        .iron(userResources != null ? userResources.getIron(): null)
                        .build())
                .build();
    }

    private Long findRankByPoints(User user) {
        return userRepository.findRankByPoints(user.getId());
    }

}
