package com.buffalosoftware.api.event;

import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RecruitmentEvent extends ApplicationEvent {

    private Long cityId;
    private Long cityBuildingId;
    private RecruitmentAction action;

    @Builder
    public RecruitmentEvent(Object source, Long cityId, Long cityBuildingId, RecruitmentAction action) {
        super(source);
        this.cityId = cityId;
        this.cityBuildingId = cityBuildingId;
        this.action = action;
    }

    public enum RecruitmentAction {
        created, completed
    }
}
