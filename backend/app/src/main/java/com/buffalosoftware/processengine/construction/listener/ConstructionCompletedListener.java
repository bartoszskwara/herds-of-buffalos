package com.buffalosoftware.processengine.construction.listener;

import com.buffalosoftware.api.city.IBuildingUpgradeService;
import com.buffalosoftware.api.event.ConstructionEvent;
import com.buffalosoftware.api.event.IEventService;
import com.buffalosoftware.api.processengine.IProcessInstanceVariableProvider;
import com.buffalosoftware.api.unit.IConstructionStatusManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.event.ConstructionEvent.ConstructionAction.completed;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CITY_ID;
import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.CONSTRUCTION_ID;

@Service
@RequiredArgsConstructor
public class ConstructionCompletedListener implements JavaDelegate {

    private final Logger LOGGER = LoggerFactory.getLogger(ConstructionCompletedListener.class);
    private final IConstructionStatusManager constructionStatusManager;
    private final IBuildingUpgradeService buildingUpgradeService;
    private final IProcessInstanceVariableProvider variableProvider;
    private final IEventService eventService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var constructionId = variableProvider.getVariable(delegateExecution, CONSTRUCTION_ID, Long.class);
        var cityId = variableProvider.getVariable(delegateExecution, CITY_ID, Long.class);
        constructionStatusManager.completeConstruction(constructionId);

        buildingUpgradeService.upgradeBuilding(constructionId);
        LOGGER.info("Construction task [{}] completed", constructionId);

        eventService.sendEvent(ConstructionEvent.builder().source(this)
                .cityId(cityId)
                .action(completed)
                .build());
    }
}
