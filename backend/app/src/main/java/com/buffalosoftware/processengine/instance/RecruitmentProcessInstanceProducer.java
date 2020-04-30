package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.AbstractProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.promotion.definition.PromotionProcessDefinitionManager;
import com.buffalosoftware.processengine.recruitment.definition.RecruitmentProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
class RecruitmentProcessInstanceProducer extends AbstractProcessInstanceProducer {

    private final RecruitmentProcessDefinitionManager definitionManager;

    public RecruitmentProcessInstanceProducer(final RuntimeService runtimeService, final ITimeService timeService, final RecruitmentProcessDefinitionManager definitionManager) {
        super(runtimeService, timeService);
        this.definitionManager = definitionManager;
    }

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.recruitment;
    }

    @Override
    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
}
