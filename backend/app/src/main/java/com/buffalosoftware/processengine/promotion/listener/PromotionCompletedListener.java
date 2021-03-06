package com.buffalosoftware.processengine.promotion.listener;

import com.buffalosoftware.api.event.IEventService;
import com.buffalosoftware.api.event.PromotionEvent;
import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IPromotionStatusManager;
import com.buffalosoftware.api.unit.IUnitPromotionService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.event.PromotionEvent.PromotionAction.completed;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.PROMOTION_ID;

@Service
@RequiredArgsConstructor
public class PromotionCompletedListener implements ExecutionListener {

    private final Logger LOGGER = LoggerFactory.getLogger(PromotionCompletedListener.class);
    private final IPromotionStatusManager promotionStatusManager;
    private final IUnitPromotionService unitPromotionService;
    private final IProcessInstanceVariableProvider variableProvider;
    private final IEventService eventService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var promotionId = variableProvider.getVariable(delegateExecution, PROMOTION_ID, Long.class);
        var cityId = variableProvider.getVariable(delegateExecution, CITY_ID, Long.class);
        var cityBuildingId = variableProvider.getVariable(delegateExecution, CITY_BUILDING_ID, Long.class);
        promotionStatusManager.completePromotion(promotionId);

        unitPromotionService.promoteUnit(promotionId);
        LOGGER.info("Promotion task [{}] completed", promotionId);

        eventService.sendEvent(PromotionEvent.builder().source(this)
                .cityId(cityId)
                .cityBuildingId(cityBuildingId)
                .action(completed)
                .build());
    }
}
