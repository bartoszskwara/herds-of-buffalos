package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.AbstractProcessInstanceProducer;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.production.definition.ProductionProcessDefinitionManager;
import com.buffalosoftware.processengine.recruitment.definition.RecruitmentProcessDefinitionManager;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
class ResourceProductionProcessInstanceProducer extends AbstractProcessInstanceProducer {

    private final ProductionProcessDefinitionManager definitionManager;

    public ResourceProductionProcessInstanceProducer(final RuntimeService runtimeService, final ITimeService timeService, final ProductionProcessDefinitionManager definitionManager) {
        super(runtimeService, timeService);
        this.definitionManager = definitionManager;
    }

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.production;
    }

    @Override
    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
}
