package com.buffalosoftware.processengine.production.definition;

import com.buffalosoftware.api.processengine.DefinitionManager;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.stereotype.Service;

import static com.buffalosoftware.api.processengine.ProcessInstanceVariable.RESOURCE_PRODUCTION_TIME;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductionProcessDefinitionManager implements DefinitionManager {

    private final static String PRODUCTION_PROCESS_DEFINITION_KEY = "production_process.bpmn";
    private final static String PRODUCTION_PROCESS_TIMER_ID = "PRODUCTION_PROCESS_TIMER";
    private final static String PRODUCTION_PROCESS_TIMER_NAME = "Wait until resource is produced";
    private final static String PRODUCTION_PROCESS_PRODUCE_RESOURCE_ID = "PRODUCTION_PROCESS_PRODUCE_RESOURCE_ID";
    private final static String PRODUCTION_PROCESS_PRODUCE_RESOURCE_NAME = "Produce resources";
    private final static String PRODUCTION_PROCESS_EXCLUSIVE_GATEWAY_ID = "PRODUCTION_PROCESS_PRODUCT_RESOURCE_ID";
    private final static String PRODUCTION_PROCESS_EXCLUSIVE_GATEWAY_NAME = "Resources produced";

    @Override
    public String getDefinitionKey() {
        return PRODUCTION_PROCESS_DEFINITION_KEY;
    }

    @Override
    public BpmnModelInstance createModelInstance() {
        return Bpmn.createExecutableProcess(PRODUCTION_PROCESS_DEFINITION_KEY)
                .name(PRODUCTION_PROCESS_DEFINITION_KEY)
                .startEvent(START_EVENT_ID)
                    .name(START_EVENT_NAME)
                .intermediateCatchEvent()
                    .id(PRODUCTION_PROCESS_TIMER_ID)
                    .name(PRODUCTION_PROCESS_TIMER_NAME)
                    .timerWithDuration(format("${%s}", RESOURCE_PRODUCTION_TIME.name()))
                .serviceTask(PRODUCTION_PROCESS_PRODUCE_RESOURCE_ID)
                    .name(PRODUCTION_PROCESS_PRODUCE_RESOURCE_NAME)
                    .camundaClass("com.buffalosoftware.processengine.production.delegate.ProduceResourcesTask")
                .parallelGateway(PRODUCTION_PROCESS_EXCLUSIVE_GATEWAY_ID)
                        .name(PRODUCTION_PROCESS_EXCLUSIVE_GATEWAY_NAME)
                        .connectTo(PRODUCTION_PROCESS_TIMER_ID)
                    .moveToNode(PRODUCTION_PROCESS_EXCLUSIVE_GATEWAY_ID)
                        .endEvent(END_EVENT_ID)
                        .name(END_EVENT_NAME)
                .done();
    }
}
