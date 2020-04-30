package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.AbstractProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.construction.definition.ConstructionProcessDefinitionManager;
import com.buffalosoftware.processengine.promotion.definition.PromotionProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
class PromotionProcessInstanceProducer extends AbstractProcessInstanceProducer {

    private final PromotionProcessDefinitionManager definitionManager;

    public PromotionProcessInstanceProducer(final RuntimeService runtimeService, final ITimeService timeService, final PromotionProcessDefinitionManager definitionManager) {
        super(runtimeService, timeService);
        this.definitionManager = definitionManager;
    }

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.promotion;
    }

    @Override
    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
}
