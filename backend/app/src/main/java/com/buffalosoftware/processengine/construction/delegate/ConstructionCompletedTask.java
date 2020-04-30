package com.buffalosoftware.processengine.construction.delegate;

import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IConstructionStatusManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_BUILDING_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RECRUITMENT_ID;

@Service
@RequiredArgsConstructor
public class ConstructionCompletedTask implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(ConstructionCompletedTask.class);
    private final IConstructionStatusManager constructionStatusManager;
    private final IBuildingUpgradeService buildingUpgradeService;
    private final IProcessInstanceVariableProvider variableProvider;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var constructionId = variableProvider.getVariable(delegateExecution, CONSTRUCTION_ID, Long.class);
        var cityId = variableProvider.getVariable(delegateExecution, CITY_ID, Long.class);
        constructionStatusManager.completeConstruction(constructionId);
        LOGGER.info("Construction task [{}] completed", constructionId);

        buildingUpgradeService.upgradeBuilding(constructionId);
        buildingUpgradeService.startNextConstructionTaskIfNotInProgress(cityId);
    }
}
