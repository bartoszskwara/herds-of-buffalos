package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.unit.PromotionProgressDto;
import com.buffalosoftware.dto.unit.UnitPromotionRequestDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;

import java.util.List;

public interface IUnitPromotionService {
    void promoteUnit(Long promotionId);
    void createPromotionTaskAndStartProcess(Long userId, Long cityId, UnitPromotionRequestDto unitPromotionRequestDto);
    List<PromotionProgressDto> getPromotionProgressByBuilding(City city, Building building);
}
