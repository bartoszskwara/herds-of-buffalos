package com.buffalosoftware.api.unit;

import com.buffalosoftware.dto.unit.RecruitmentDto;
import com.buffalosoftware.dto.unit.RecruitmentProgressDto;
import com.buffalosoftware.entity.Building;
import com.buffalosoftware.entity.City;
import com.buffalosoftware.entity.CityBuilding;
import com.buffalosoftware.entity.Recruitment;

import java.util.List;

public interface IUnitRecruitmentService {
    void recruit(Long recruitmentId, Integer amount);
    void createRecruitmentTaskAndStartProcess(Long userId, Long cityId, RecruitmentDto recruitmentDto);
    List<RecruitmentProgressDto> getCityRecruitmentProgress(Long userId, Long cityId);
    List<RecruitmentProgressDto> getRecruitmentProgressByBuilding(City city, Building building);
}
