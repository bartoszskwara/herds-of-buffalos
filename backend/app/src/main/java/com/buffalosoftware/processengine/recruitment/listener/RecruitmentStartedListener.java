package com.buffalosoftware.processengine.recruitment.listener;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IRecruitmentStatusManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;

@Service
@RequiredArgsConstructor
public class RecruitmentStartedListener implements ExecutionListener {

    private Logger LOGGER = LoggerFactory.getLogger(RecruitmentStartedListener.class);
    private final IRecruitmentStatusManager recruitmentManager;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        var recruitmentId = variableProvider.getVariable(delegateExecution, RECRUITMENT_ID, Long.class);
        recruitmentManager.startRecruitment(recruitmentId);
        LOGGER.info("Recruitment task [{}] started", recruitmentId);
    }
}
