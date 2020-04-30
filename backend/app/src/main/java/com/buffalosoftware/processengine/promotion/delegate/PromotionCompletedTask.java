package com.buffalosoftware.processengine.promotion.delegate;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IPromotionStatusManager;
import com.buffalosoftware.api.unit.IUnitPromotionService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.PROMOTION_ID;

@Service
@RequiredArgsConstructor
public class PromotionCompletedTask implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(PromotionCompletedTask.class);
    private final IPromotionStatusManager promotionStatusManager;
    private final IUnitPromotionService unitPromotionService;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var promotionId = variableProvider.getVariable(delegateExecution, PROMOTION_ID, Long.class);
        var cityBuildingId = variableProvider.getVariable(delegateExecution, CITY_BUILDING_ID, Long.class);
        promotionStatusManager.completePromotion(promotionId);

        unitPromotionService.promoteUnit(promotionId);
        LOGGER.info("Promotion task [{}] completed", promotionId);

        unitPromotionService.startNextPromotionTaskIfNotInProgress(cityBuildingId);
    }
}
