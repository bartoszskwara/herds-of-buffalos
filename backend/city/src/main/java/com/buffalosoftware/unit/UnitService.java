package com.buffalosoftware.unit;

import com.buffalosoftware.api.unit.IUnitService;
import com.buffalosoftware.dto.building.CityUnitDto;
import com.buffalosoftware.entity.User;
import com.buffalosoftware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UnitService implements IUnitService {

    private final UserRepository userRepository;

    @Override
    public List<CityUnitDto> getUnitsInCity(Long userId, Long cityId) {
        User user = userRepository.findUserWithCitiesAndCityUnitsById(userId).orElseThrow(() -> new IllegalArgumentException("User doesn't exist!"));
        return user.getCities().stream()
                .filter(city -> city.getId().equals(cityId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("City doesn't exist!"))
                .getCityUnits().stream()
                .map(units -> CityUnitDto.builder()
                        .key(units.getUnit().name())
                        .label(units.getUnit().getName())
                        .level(units.getLevel())
                        .amount(units.getAmount())
                        .building(Unit.getBuildingByUnit(units.getUnit()).map(Enum::name).orElse(null))
                        .build())
                .collect(toList());
    }
}
