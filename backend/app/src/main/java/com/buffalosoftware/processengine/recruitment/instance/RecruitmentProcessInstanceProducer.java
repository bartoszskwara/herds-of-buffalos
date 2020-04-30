package com.buffalosoftware.processengine.recruitment.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.api.processengine.IProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.api.processengine.ProcessInstanceVariable;
import com.buffalosoftware.processengine.recruitment.definition.RecruitmentProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_RECRUITMENT_TIME;

@Service
@RequiredArgsConstructor
class RecruitmentProcessInstanceProducer extends IProcessInstanceProducer {

    private final RecruitmentProcessDefinitionManager definitionManager;
    private final RuntimeService runtimeService;
    private final ITimeService timeService;

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.recruitment;
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
