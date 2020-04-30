package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.IProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.promotion.definition.PromotionProcessDefinitionManager;
import com.buffalosoftware.processengine.recruitment.definition.RecruitmentProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class PromotionProcessInstanceProducer extends IProcessInstanceProducer {

    private final PromotionProcessDefinitionManager definitionManager;
    private final RuntimeService runtimeService;
    private final ITimeService timeService;

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.promotion;
    }

    @Override
    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }

    @Override
    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    @Override
    public ITimeService getTimeService() {
        return timeService;
    }
}
