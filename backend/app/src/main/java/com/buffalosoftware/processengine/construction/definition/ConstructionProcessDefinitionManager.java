package com.buffalosoftware.processengine.construction.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.BUILDING_CONSTRUCTION_TIME;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ConstructionProcessDefinitionManager implements DefinitionManager {

    private final static String CONSTRUCTION_PROCESS_DEFINITION_KEY = "construction_process.bpmn";
    private final static String CONSTRUCTION_PROCESS_TIMER_ID = "CONSTRUCTION_PROCESS_TIMER";
    private final static String CONSTRUCTION_PROCESS_TIMER_NAME = "Wait until bulding is built";
    private final static String CONSTRUCTION_PROCESS_START_TASK_ID = "CONSTRUCTION_PROCESS_START_TASK";
    private final static String CONSTRUCTION_PROCESS_START_TASK_NAME = "Set status of construction to InProgress";
    private final static String CONSTRUCTION_PROCESS_COMPLETED_TASK_ID = "CONSTRUCTION_PROCESS_COMPLETED_TASK";
    private final static String CONSTRUCTION_PROCESS_COMPLETED_TASK_NAME = "Set status of construction to Completed";

    @Override
    public String getDefinitionKey() {
        return CONSTRUCTION_PROCESS_DEFINITION_KEY;
    }

    @Override
    public BpmnModelInstance createModelInstance() {
        return Bpmn.createExecutableProcess(CONSTRUCTION_PROCESS_DEFINITION_KEY)
                .name(CONSTRUCTION_PROCESS_DEFINITION_KEY)
                .startEvent(START_EVENT_ID)
                    .name(START_EVENT_NAME)
                .serviceTask(CONSTRUCTION_PROCESS_START_TASK_ID)
                    .name(CONSTRUCTION_PROCESS_START_TASK_NAME)
                    .camundaClass("com.buffalosoftware.processengine.construction.delegate.ConstructionStartedTask")
                .intermediateCatchEvent()
                    .id(CONSTRUCTION_PROCESS_TIMER_ID)
                    .name(CONSTRUCTION_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", BUILDING_CONSTRUCTION_TIME.name()))
                .serviceTask(CONSTRUCTION_PROCESS_COMPLETED_TASK_ID)
                    .name(CONSTRUCTION_PROCESS_COMPLETED_TASK_NAME)
                    .camundaClass("com.buffalosoftware.processengine.construction.delegate.ConstructionCompletedTask")
                .endEvent(END_EVENT_ID)
                    .name(END_EVENT_NAME)
                .done();
    }
}
