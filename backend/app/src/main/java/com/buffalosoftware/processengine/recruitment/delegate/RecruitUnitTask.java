package com.buffalosoftware.processengine.recruitment.delegate;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.UNIT_AMOUNT_LEFT;

@Service
@RequiredArgsConstructor
public class RecruitUnitTask implements JavaDelegate {

    Logger LOGGER = LoggerFactory.getLogger(RecruitUnitTask.class);
    private final IProcessInstanceVariableProvider variableProvider;
    private final IUnitRecruitmentService unitRecruitmentService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var amountLeft = variableProvider.getVariable(delegateExecution, UNIT_AMOUNT_LEFT, Integer.class);
        var recruitmentId = variableProvider.getVariable(delegateExecution, RECRUITMENT_ID, Long.class);

        unitRecruitmentService.recruit(recruitmentId, 1);
        delegateExecution.setVariable(UNIT_AMOUNT_LEFT.name(), amountLeft - 1);

        LOGGER.debug("1 unit recruited. Amount left: [{}]", amountLeft - 1);
    }
}
