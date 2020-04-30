package com.buffalosoftware.api.processengine;

import java.util.Map;

public interface IProcessInstanceProducer {
    String createProcessInstance(ProcessInstanceVariablesDto processInstanceVariablesDto);
    ProcessType supportedProcessType();
}
