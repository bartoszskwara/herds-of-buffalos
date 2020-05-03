package com.buffalosoftware.processengine.recruitment.listener;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IRecruitmentStatusManager;
import com.buffalosoftware.api.unit.IUnitRecruitmentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;

@Service
@RequiredArgsConstructor
public class RecruitmentCompletedListener implements ExecutionListener {

    private Logger LOGGER = LoggerFactory.getLogger(RecruitmentCompletedListener.class);
    private final IRecruitmentStatusManager recruitmentStatusManager;
    private final IUnitRecruitmentService unitRecruitmentService;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var recruitmentId = variableProvider.getVariable(delegateExecution, RECRUITMENT_ID, Long.class);
        var cityBuildingId = variableProvider.getVariable(delegateExecution, CITY_BUILDING_ID, Long.class);
        recruitmentStatusManager.completeRecruitment(recruitmentId);
        LOGGER.info("Recruitment task [{}] completed", recruitmentId);

        unitRecruitmentService.startNextRecruitmentTaskIfNotInProgress(cityBuildingId);
    }
}
