package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.processengine.AbstractProcessInstanceProducer;
import com.buffalosoftware.api.processengine.IProcessInstanceProducerProvider;
import com.buffalosoftware.api.processengine.ProcessType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ProcessInstanceProducerProvider implements IProcessInstanceProducerProvider {

    private static Map<ProcessType, AbstractProcessInstanceProducer> processInstanceProducers = new HashMap<>();

    @Override
    public AbstractProcessInstanceProducer getProducer(ProcessType processType) {
        return ofNullable(processInstanceProducers.get(processType)).orElseThrow(() -> new IllegalArgumentException("Process type is not supported!"));
    }

    @Autowired
    public void setProcessInstanceProducers(List<AbstractProcessInstanceProducer> providers) {
        processInstanceProducers = providers.stream().collect(toMap(AbstractProcessInstanceProducer::supportedProcessType, Function.identity()));
    }

}
