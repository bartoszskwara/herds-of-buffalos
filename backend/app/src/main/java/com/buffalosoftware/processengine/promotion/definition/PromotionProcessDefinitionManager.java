package com.buffalosoftware.processengine.promotion.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.processengine.promotion.listener.PromotionCompletedListener;
import com.buffalosoftware.processengine.promotion.listener.PromotionStartedListener;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.ExecutionListener;
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
                    .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_START, PromotionStartedListener.class.getName())
                .intermediateCatchEvent()
                    .id(PROMOTION_PROCESS_TIMER_ID)
                    .name(PROMOTION_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", UNIT_PROMOTION_TIME.name()))
                .endEvent(END_EVENT_ID)
                    .name(END_EVENT_NAME)
                    .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_END, PromotionCompletedListener.class.getName())
                .done();
    }
}
