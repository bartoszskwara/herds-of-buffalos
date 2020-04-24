package com.buffalosoftware.common;

import com.buffalosoftware.Cost;
import com.buffalosoftware.dto.resources.ResourcesDto;

public class CostMapper {
    public static ResourcesDto mapCost(Cost cost) {
        if(cost == null) {
            return null;
        }
        return ResourcesDto.builder()
                .wood(cost.getWood())
                .clay(cost.getClay())
                .iron(cost.getIron())
                .build();
    }
}
