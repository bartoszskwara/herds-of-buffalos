package com.buffalosoftware.processengine.promotion.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_PROMOTION_TIME;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class PromotionProcessDefinitionManager implements DefinitionManager {

    private final static String PROMOTION_PROCESS_DEFINITION_KEY = "promotion_process.bpmn";
    private final static String PROMOTION_PROCESS_TIMER_ID = "PROMOTION_PROCESS_TIMER";
    private final static String PROMOTION_PROCESS_TIMER_NAME = "Wait until unit is promoted";
    private final static String PROMOTION_PROCESS_START_TASK_ID = "PROMOTION_PROCESS_START_TASK";
    private final static String PROMOTION_PROCESS_START_TASK_NAME = "Set status of promotion to InProgress";
    private final static String PROMOTION_PROCESS_COMPLETED_TASK_ID = "PROMOTION_PROCESS_COMPLETED_TASK";
    private final static String PROMOTION_PROCESS_COMPLETED_TASK_NAME = "Set status of promotion to Completed";

    @Override
    public String getDefinitionKey() {
        return PROMOTION_PROCESS_DEFINITION_KEY;
    }

    @Override
    public BpmnModelInstance createModelInstance() {
        return Bpmn.createExecutableProcess(PROMOTION_PROCESS_DEFINITION_KEY)
                .name(PROMOTION_PROCESS_DEFINITION_KEY)
                .startEvent(START_EVENT_ID)
                    .name(START_EVENT_NAME)
                .serviceTask(PROMOTION_PROCESS_START_TASK_ID)
                    .name(PROMOTION_PROCESS_START_TASK_NAME)
                    .camundaClass("com.buffalosoftware.processengine.promotion.delegate.PromotionStartedTask")
                .intermediateCatchEvent()
                    .id(PROMOTION_PROCESS_TIMER_ID)
                    .name(PROMOTION_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", UNIT_PROMOTION_TIME.name()))
                .serviceTask(PROMOTION_PROCESS_COMPLETED_TASK_ID)
                    .name(PROMOTION_PROCESS_COMPLETED_TASK_NAME)
                    .camundaClass("com.buffalosoftware.processengine.promotion.delegate.PromotionCompletedTask")
                .endEvent(END_EVENT_ID)
                    .name(END_EVENT_NAME)
                .done();
    }
}
