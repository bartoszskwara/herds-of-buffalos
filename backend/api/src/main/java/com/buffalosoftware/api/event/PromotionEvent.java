package com.buffalosoftware.api.event;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PromotionEvent extends ApplicationEvent {

    private Long cityId;
    private Long cityBuildingId;
    private PromotionAction action;

    @Builder
    public PromotionEvent(Object source, Long cityId, Long cityBuildingId, PromotionAction action) {
        super(source);
        this.cityId = cityId;
        this.cityBuildingId = cityBuildingId;
        this.action = action;
    }

    public enum PromotionAction {
        created, completed
    }
}
