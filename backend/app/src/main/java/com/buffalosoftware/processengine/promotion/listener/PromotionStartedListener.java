package com.buffalosoftware.processengine.promotion.listener;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IPromotionStatusManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.PROMOTION_ID;

@Service
@RequiredArgsConstructor
public class PromotionStartedListener implements ExecutionListener {

    private final Logger LOGGER = LoggerFactory.getLogger(PromotionStartedListener.class);
    private final IPromotionStatusManager promotionStatusManager;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var promotionId = variableProvider.getVariable(delegateExecution, PROMOTION_ID, Long.class);
        promotionStatusManager.startPromotion(promotionId);
        LOGGER.info("Promotion task [{}] started", promotionId);
    }
}
