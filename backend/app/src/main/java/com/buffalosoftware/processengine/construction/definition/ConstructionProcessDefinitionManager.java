package com.buffalosoftware.processengine.construction.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.processengine.construction.listener.ConstructionCompletedListener;
import com.buffalosoftware.processengine.construction.listener.ConstructionStartedListener;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.ExecutionListener;
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
    private final static String CONSTRUCTION_PROCESS_TIMER_NAME = "Wait until building is built";

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
                    .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_START, ConstructionStartedListener.class.getName())
                .intermediateCatchEvent()
                    .id(CONSTRUCTION_PROCESS_TIMER_ID)
                    .name(CONSTRUCTION_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", BUILDING_CONSTRUCTION_TIME.name()))
                    .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_END, ConstructionCompletedListener.class.getName())
                .endEvent(END_EVENT_ID)
                    .name(END_EVENT_NAME)
                .done();
    }
}
