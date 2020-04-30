package com.buffalosoftware.api.processengine;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public interface DefinitionManager {
    String START_EVENT_ID = "START_EVENT";
    String START_EVENT_NAME = "Start process";
    String END_EVENT_ID = "END_EVENT";
    String END_EVENT_NAME = "End process";

    String getDefinitionKey();
    BpmnModelInstance createModelInstance();
}
