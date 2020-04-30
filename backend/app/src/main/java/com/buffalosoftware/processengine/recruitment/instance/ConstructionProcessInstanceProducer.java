package com.buffalosoftware.processengine.recruitment.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.IProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.processengine.construction.definition.ConstructionProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ConstructionProcessInstanceProducer extends IProcessInstanceProducer {

    private final ConstructionProcessDefinitionManager definitionManager;
    private final RuntimeService runtimeService;
    private final ITimeService timeService;

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.construction;
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
