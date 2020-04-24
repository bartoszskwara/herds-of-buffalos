package com.buffalosoftware.city;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.dto.building.BuildingDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.dto.city.CityDataDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.UserRepository;
import com.buffalosoftware.resource.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CityService implements ICityService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ResourceService resourceService;

    @Override
    public List<City> getAllUserCities(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        return cityRepository.findCitiesByUser_Id(user.getId());
    }

    @Override
    public CityDataDto getCityData(Long userId, Long cityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        City city = user.getCities().stream()
                .filter(c -> c.getId().equals(cityId)).findFirst().orElseThrow(() -> new IllegalArgumentException("User doesn't own the city!"));
        return CityDataDto.builder()
                .id(city.getId())
                .name(city.getName())
                .points(city.getPoints())
                .resources(ResourcesDto.builder()
                        .wood(getResourceAmount(city.getCityResources(), wood))
                        .clay(getResourceAmount(city.getCityResources(), clay))
                        .iron(getResourceAmount(city.getCityResources(), iron))
                        .build())
                .buildings(findBuildingsInCity(city))
                .build();
    }

    private List<CityBuildingDto> findBuildingsInCity(City city) {
        return Building.list().stream()
                .map(b -> {
                    Optional<CityBuilding> cityBuilding = city.getCityBuildings().stream().filter(cb -> b.equals(cb.getBuilding())).findFirst();
                    return CityBuildingDto.builder()
                            .building(BuildingDto.builder()
                                    .key(b)
                                    .label(b.getName())
                                    .build())
                            .enabled(cityBuilding.isPresent())
                            .currentLevel(cityBuilding.map(CityBuilding::getLevel).orElse(0))
                            .build();
                })
                .collect(toList());
    }

    @Override
    public City createFreshCity(User user) {
        /*City city = new City();
        city.setUser(user);
        city.setName("City of " + user.getName());
        city.setCityResources(resourceService.getStartingResources());
        provideRandomFreeCoords();
        city.setCoordsX();
        city.setCoordsY();*/
        return null;
    }

    private Integer getResourceAmount(Set<CityResources> cityResources, Resource resource) {
        Optional<CityResources> resourceData = cityResources.stream().filter(r -> resource.equals(r.getResource())).findFirst();
        if(resourceData.isPresent()) {
            return resourceData.get().getAmount();
        }
        return 0;
    }

}
