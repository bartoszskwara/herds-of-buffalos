package com.buffalosoftware.processengine.construction.listener;

import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IConstructionStatusManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;

@Service
@RequiredArgsConstructor
public class ConstructionStartedListener implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(ConstructionStartedListener.class);
    private final IConstructionStatusManager constructionStatusManager;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var constructionId = variableProvider.getVariable(delegateExecution, CONSTRUCTION_ID, Long.class);
        constructionStatusManager.startConstruction(constructionId);
        LOGGER.info("Construction task [{}] started", constructionId);
    }
}
