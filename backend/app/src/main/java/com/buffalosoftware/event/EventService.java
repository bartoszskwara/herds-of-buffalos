package com.buffalosoftware.event;

import com.buffalosoftware.api.event.BuildingEvent;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.event.IEventService;
import com.buffalosoftware.api.event.PromotionEvent;
import com.buffalosoftware.api.event.RecruitmentEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendEvent(BuildingEvent event) {
        LOGGER.info("Publishing building event [building={}] [action={}]", event.getBuilding(), event.getAction());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void sendEvent(ConstructionEvent event) {
        LOGGER.info("Publishing construction event [action={}]", event.getAction());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void sendEvent(RecruitmentEvent event) {
        LOGGER.info("Publishing recruitment event [action={}]", event.getAction());
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void sendEvent(PromotionEvent event) {
        LOGGER.info("Publishing promotion event [action={}]", event.getAction());
        applicationEventPublisher.publishEvent(event);
    }

}
