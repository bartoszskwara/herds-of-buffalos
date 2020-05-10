package com.buffalosoftware.api.event;

public interface IEventService {
    void sendEvent(BuildingEvent event);
    void sendEvent(ConstructionEvent event);
    void sendEvent(RecruitmentEvent event);
    void sendEvent(PromotionEvent event);
}
