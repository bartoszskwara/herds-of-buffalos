package com.buffalosoftware.user;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.api.user.IUserService;
import com.buffalosoftware.dto.UserPointsDto;
import com.buffalosoftware.dto.user.CreateUserRequestDto;
import com.buffalosoftware.dto.user.UserDto;
import com.buffalosoftware.entity.BaseEntity;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.buffalosoftware.entity.Building.barracks;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ICityService cityService;

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
                .filter(u -> u.getCities().stream()
                        .map(City::getCityUnits)
                        .flatMap(Collection::stream)
                        .map(cu -> cu.getUnit().getBuilding())
                        .distinct().count() > 1)
                .findFirst()
                .orElse(allUsers.stream()
                        .filter(u -> u.getCities().size() > 1)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No users in database!")));
        return mapToDto(user);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public void createUser(CreateUserRequestDto createUserRequestDto) {
        userRepository.findByName(createUserRequestDto.getName())
                .ifPresent(s -> { throw new IllegalArgumentException("Name already exists!"); });
        userRepository.findByEmail(createUserRequestDto.getEmail())
                .ifPresent(s -> { throw new IllegalArgumentException("Email already exists!"); });

        User user = new User();
        user.setName(createUserRequestDto.getName());
        user.setEmail(createUserRequestDto.getEmail());
        City city = cityService.createFreshCity(user);

        //TODO
        throw new IllegalArgumentException("Functionality not implemented!");
    }

    private Integer findRankByPoints(User user) {
        List<UserPointsDto> userPoints = userRepository.findUsersWithPoints().stream()
                .sorted(Comparator.comparing(UserPointsDto::getPoints, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return IntStream.range(0, userPoints.size())
                .filter(i -> user.getId().equals(userPoints.get(i).getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User does not exist!"));
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .points(user.getCities().stream().mapToLong(City::getPoints).sum())
                .ranking(findRankByPoints(user))
                .numberOfCities(user.getCities().size())
                .currentCityId(user.getCities().stream().max(Comparator.comparing(c -> c.getCityUnits().size())).map(BaseEntity::getId).orElse(null))
                .build();
    }
}
