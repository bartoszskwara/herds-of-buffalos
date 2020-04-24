package com.buffalosoftware.api.city;

import com.buffalosoftware.dto.ProgressTaskDto;
import com.buffalosoftware.dto.building.ConstructionProgressDto;
import com.buffalosoftware.dto.building.CityBuildingDto;
import com.buffalosoftware.entity.Building;

import java.util.List;

public interface IBuildingService {
    List<CityBuildingDto> getCityBuildings(Long userId, Long cityId);
    List<ConstructionProgressDto> getAllConstructionsProgressInCity(Long userId, Long cityId);
    List<ProgressTaskDto> getTasks(Long userId, Long cityId, Building building);
}
