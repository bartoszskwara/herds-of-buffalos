package com.buffalosoftware.api.processengine;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.Execution;

public interface IProcessInstanceVariableProvider {
    <T> T getVariable(DelegateExecution execution, ProcessInstanceVariable processInstanceVariable, Class<T> variableClass);
    <T> T getVariable(Execution execution, ProcessInstanceVariable processInstanceVariable, Class<T> variableClass);
}
