package com.buffalosoftware.processengine.recruitment.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import com.buffalosoftware.processengine.recruitment.listener.RecruitmentCompletedListener;
import com.buffalosoftware.processengine.recruitment.listener.RecruitmentStartedListener;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_RECRUITMENT_TIME;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessDefinitionManager implements DefinitionManager {

    private final static String RECRUITMENT_PROCESS_DEFINITION_KEY = "recruitment_process.bpmn";
    private final static String RECRUITMENT_PROCESS_TIMER_ID = "RECRUITMENT_PROCESS_TIMER";
    private final static String RECRUITMENT_PROCESS_TIMER_NAME = "Wait until one unit is recruited";
    private final static String RECRUITMENT_PROCESS_RECRUIT_UNIT_TASK_ID = "RECRUITMENT_PROCESS_RECRUIT_UNIT_TASK";
    private final static String RECRUITMENT_PROCESS_RECRUIT_UNIT_TASK_NAME = "Increase numbner of units";
    private final static String RECRUITMENT_PROCESS_CONDITION_ID = "RECRUITMENT_PROCESS_CONDITION";
    private final static String RECRUITMENT_PROCESS_CONDITION_NAME = "Are there any units left to be recruited?";

    @Override
    public String getDefinitionKey() {
        return RECRUITMENT_PROCESS_DEFINITION_KEY;
    }

    @Override
    public BpmnModelInstance createModelInstance() {
        return Bpmn.createExecutableProcess(RECRUITMENT_PROCESS_DEFINITION_KEY)
                .name(RECRUITMENT_PROCESS_DEFINITION_KEY)
                .startEvent(START_EVENT_ID)
                    .name("Start recruitment process")
                    .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_START, RecruitmentStartedListener.class.getName())
                .intermediateCatchEvent()
                    .id(RECRUITMENT_PROCESS_TIMER_ID)
                    .name(RECRUITMENT_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", UNIT_RECRUITMENT_TIME.name()))
                .serviceTask(RECRUITMENT_PROCESS_RECRUIT_UNIT_TASK_ID)
                    .name(RECRUITMENT_PROCESS_RECRUIT_UNIT_TASK_NAME)
                    .camundaClass("com.buffalosoftware.processengine.recruitment.delegate.RecruitUnitTask")
                .exclusiveGateway(RECRUITMENT_PROCESS_CONDITION_ID)
                    .name(RECRUITMENT_PROCESS_CONDITION_NAME)
                        .condition("yes", format("${%s>0}", UNIT_AMOUNT_LEFT.name()))
                        .connectTo(RECRUITMENT_PROCESS_TIMER_ID)
                    .moveToNode(RECRUITMENT_PROCESS_CONDITION_ID)
                        .condition("no", format("${%s<=0}", UNIT_AMOUNT_LEFT.name()))
                        .endEvent(END_EVENT_ID)
                            .name("End recruitment process")
                            .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_END, RecruitmentCompletedListener.class.getName())
                .done();
    }
}
