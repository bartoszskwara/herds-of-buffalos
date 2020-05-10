package com.buffalosoftware.api.event;

import com.buffalosoftware.entity.Building;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConstructionEvent extends ApplicationEvent {

    private ConstructionAction action;
    private Long cityId;

    @Builder
    public ConstructionEvent(Object source, Long cityId, ConstructionAction action) {
        super(source);
        this.cityId = cityId;
        this.action = action;
    }

    public enum ConstructionAction {
        created, completed
    }
}
