package com.buffalosoftware.city;

import com.buffalosoftware.api.city.ICityService;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.CityRepository;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService implements ICityService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Override
    public List<City> getAllUserCities(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        return cityRepository.findCitiesByUser_Id(userId);
    }

}
