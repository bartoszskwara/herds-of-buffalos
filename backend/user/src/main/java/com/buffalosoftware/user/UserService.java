package com.buffalosoftware.user;

import com.buffalosoftware.api.user.IUserService;
import com.buffalosoftware.dto.building.UserDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.buffalosoftware.entity.Building.barracks;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto findRandomUserWithMultipleCities() {
        List<User> allUsers = userRepository.findAll();
        User user = allUsers.stream()
                .filter(u -> u.getCities().size() > 1)
                .filter(u -> u.getCities().stream()
                        .anyMatch(city -> city.getCityBuildings().stream()
                                .anyMatch(cityBuilding -> barracks.equals(cityBuilding.getBuilding()))))
                .findFirst()
                .orElse(allUsers.stream()
                        .filter(u -> u.getCities().size() > 1)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No users in database!")));
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .points(user.getCities().stream().mapToLong(City::getPoints).sum())
                .ranking(findRankByPoints(user))
                .numberOfCities(user.getCities().size())
                .currentCityId(user.getCities().stream().findAny().map(c -> c.getId()).orElse(null))
                .build();
    }

    private Long findRankByPoints(User user) {
        return userRepository.findRankByPoints(user.getId());
    }

}
