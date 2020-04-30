package com.buffalosoftware.processengine;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public interface DefinitionManager {
    String getDefinitionKey();
    BpmnModelInstance createModelInstance();
}
