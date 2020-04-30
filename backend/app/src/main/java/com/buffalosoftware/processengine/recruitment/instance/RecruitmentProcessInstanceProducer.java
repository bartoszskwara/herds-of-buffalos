package com.buffalosoftware.processengine.recruitment.instance;

import com.buffalosoftware.api.ITimeService;
import com.buffalosoftware.api.processengine.IProcessInstanceProducer;
import com.buffalosoftware.api.processengine.ProcessInstanceVariablesDto;
import com.buffalosoftware.api.processengine.ProcessType;
import com.buffalosoftware.api.processengine.ProcessInstanceVariable;
import com.buffalosoftware.processengine.recruitment.definition.RecruitmentProcessDefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_RECRUITMENT_TIME;

@Service
@RequiredArgsConstructor
class RecruitmentProcessInstanceProducer implements IProcessInstanceProducer {

    private final RuntimeService runtimeService;
    private final RecruitmentProcessDefinitionManager definitionManager;
    private final ITimeService timeService;

    @Override
    public String createProcessInstance(ProcessInstanceVariablesDto processInstanceVariablesDto) {
        Map<ProcessInstanceVariable, Object> variables = processInstanceVariablesDto.getVariables();

        ProcessInstantiationBuilder processInstanceBuilder = runtimeService.createProcessInstanceByKey(definitionManager.getDefinitionKey());
        variables.forEach((variable, value) -> {
            if(UNIT_RECRUITMENT_TIME.equals(variable)) {
                Long unitRecruitmentTime = (Long) variables.get(UNIT_RECRUITMENT_TIME);
                processInstanceBuilder.setVariable(UNIT_RECRUITMENT_TIME.name(), timeService.toSecondsISOCamundaFormat(unitRecruitmentTime));
            } else {
                processInstanceBuilder.setVariable(variable.name(), variables.get(variable));
            }
        });
        return processInstanceBuilder
                .execute()
                .getProcessInstanceId();
    }

    @Override
    public ProcessType supportedProcessType() {
        return ProcessType.recruitment;
    }


}
