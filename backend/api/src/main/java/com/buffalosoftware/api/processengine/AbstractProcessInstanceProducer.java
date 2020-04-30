package com.buffalosoftware.api.processengine;

import com.buffalosoftware.api.ITimeService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractProcessInstanceProducer {

    protected final RuntimeService runtimeService;
    protected final ITimeService timeService;

    public String createProcessInstance(ProcessInstanceVariablesDto processInstanceVariablesDto) {
        Map<ProcessInstanceVariable, Object> variables = processInstanceVariablesDto.getVariables();

        ProcessInstantiationBuilder processInstanceBuilder = runtimeService.createProcessInstanceByKey(getDefinitionManager().getDefinitionKey());
        variables.forEach((variable, value) -> {
            Object variableValue = variables.get(variable);
            if(variable.isTimeValue()) {
                variableValue = timeService.toSecondsISOCamundaFormat((Long) value);
            }
            processInstanceBuilder.setVariable(variable.name(), variableValue);
        });
        return processInstanceBuilder
                .execute()
                .getProcessInstanceId();
    }

    public abstract ProcessType supportedProcessType();
    public abstract DefinitionManager getDefinitionManager();
}
