package com.buffalosoftware.api.processengine;

import com.buffalosoftware.api.ITimeService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;

import java.util.Map;

public abstract class IProcessInstanceProducer {

    public String createProcessInstance(ProcessInstanceVariablesDto processInstanceVariablesDto) {
        Map<ProcessInstanceVariable, Object> variables = processInstanceVariablesDto.getVariables();

        ProcessInstantiationBuilder processInstanceBuilder = getRuntimeService().createProcessInstanceByKey(getDefinitionManager().getDefinitionKey());
        variables.forEach((variable, value) -> {
            Object variableValue = variables.get(variable);
            if(variable.isTimeValue()) {
                variableValue = getTimeService().toSecondsISOCamundaFormat((Long) value);
            }
            processInstanceBuilder.setVariable(variable.name(), variableValue);
        });
        return processInstanceBuilder
                .execute()
                .getProcessInstanceId();
    }

    public abstract ProcessType supportedProcessType();
    public abstract DefinitionManager getDefinitionManager();
    public abstract RuntimeService getRuntimeService();
    public abstract ITimeService getTimeService();
}
