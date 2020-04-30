package com.buffalosoftware.processengine;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.processengine.ProcessInstanceVariable;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProcessInstanceVariableProvider implements IProcessInstanceVariableProvider {

    private final RuntimeService runtimeService;

    @Override
    public <T> T getVariable(DelegateExecution execution, ProcessInstanceVariable processInstanceVariable, Class<T> variableClass) {
        Object variable = execution.getVariable(processInstanceVariable.name());
        return mapVariable(variable, processInstanceVariable, variableClass);
    }

    @Override
    public <T> T getVariable(Execution execution, ProcessInstanceVariable processInstanceVariable, Class<T> variableClass) {
        Object variable = runtimeService.getVariable(execution.getId(), UNIT_AMOUNT_LEFT.name());
        return mapVariable(variable, processInstanceVariable, variableClass);
    }

    private <T> T mapVariable(Object variable, ProcessInstanceVariable processInstanceVariable, Class<T> variableClass) {
        if (variable == null) {
            return null;
        }

        if (variable.getClass() != variableClass) {
            throw new IllegalArgumentException(format("Variable [%s] is not type %s", processInstanceVariable.name(), variableClass.getName()));
        }

        return ((T) variable);
    }
}
