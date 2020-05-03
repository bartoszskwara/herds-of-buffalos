package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.AbstractProcessInstanceProducer;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.construction.definition.ConstructionProcessDefinitionManager;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
class ConstructionProcessInstanceProducer extends AbstractProcessInstanceProducer {
    private final ConstructionProcessDefinitionManager definitionManager;

    public ConstructionProcessInstanceProducer(final RuntimeService runtimeService, final ITimeService timeService, final ConstructionProcessDefinitionManager definitionManager) {
        super(runtimeService, timeService);
        this.definitionManager = definitionManager;
    }

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.construction;
    }

    @Override
    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
}
