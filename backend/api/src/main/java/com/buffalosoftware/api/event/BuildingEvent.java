package com.buffalosoftware.api.event;

import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BuildingEvent extends ApplicationEvent {

    private Building building;
    private Long cityId;
    private Integer level;
    private BuildingAction action;

    @Builder
    public BuildingEvent(Object source, Building building, Long cityId, Integer level, BuildingAction action) {
        super(source);
        this.building = building;
        this.cityId = cityId;
        this.level = level;
        this.action = action;
    }
}
