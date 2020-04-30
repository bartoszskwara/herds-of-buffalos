package com.buffalosoftware.processengine.recruitment.delegate;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IRecruitmentStatusManager;
import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;

@Service
@RequiredArgsConstructor
public class RecruitmentCompletedTask implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(RecruitmentCompletedTask.class);
    private final IRecruitmentStatusManager recruitmentManager;
    private final IUnitRecruitmentService unitRecruitmentService;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var recruitmentId = variableProvider.getVariable(delegateExecution, RECRUITMENT_ID, Long.class);
        var cityBuildingId = variableProvider.getVariable(delegateExecution, CITY_BUILDING_ID, Long.class);
        recruitmentManager.completeRecruitment(recruitmentId);
        LOGGER.info("Recruitment task [{}] completed", recruitmentId);

        unitRecruitmentService.startNextRecruitmentTaskIfNotInProgress(cityBuildingId);
    }
}
