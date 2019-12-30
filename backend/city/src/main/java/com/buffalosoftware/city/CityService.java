package com.buffalosoftware.city;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.dto.city.CityDataDto;
import com.buffalosoftware.dto.resources.ResourcesDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityResources;
import com.buffalosoftware.entity.Resource;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.buffalosoftware.entity.Resource.clay;
import static com.buffalosoftware.entity.Resource.iron;
import static com.buffalosoftware.entity.Resource.wood;

@Service
@RequiredArgsConstructor
public class CityService implements ICityService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

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
                .build();
    }

    private Integer getResourceAmount(Set<CityResources> cityResources, Resource resource) {
        Optional<CityResources> resourceData = cityResources.stream().filter(r -> resource.equals(r.getResource())).findFirst();
        if(resourceData.isPresent()) {
            return resourceData.get().getAmount();
        }
        return 0;
    }

}
