package com.buffalosoftware.dto.user;

import com.buffalosoftware.dto.BaseDto;
import com.buffalosoftware.dto.building.UserBuildingDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDataDto extends BaseDto {
    private Long id;
    private String name;
    private Long points;
    private Integer numberOfCities;
    private Long ranking;
    private Resources resources;

    @Getter
    @Builder
    public static class Resources {
        private Long wood;
        private Long clay;
        private Long iron;
    }
}
