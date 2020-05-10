package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.city.CityDataDto;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.User;

import java.util.List;
import java.util.Optional;

public interface ICityService {
    List<City> getAllUserCities(Long userId);
    CityDataDto getCityData(Long userId, Long cityId);
    City createFreshCity(User user);
    City findCityById(Long cityId);
}
