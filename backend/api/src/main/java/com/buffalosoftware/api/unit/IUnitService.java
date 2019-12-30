package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.building.CityUnitDto;

import java.util.List;

public interface IUnitService {
    List<CityUnitDto> getUnitsInCity(Long userId, Long cityId);
}
