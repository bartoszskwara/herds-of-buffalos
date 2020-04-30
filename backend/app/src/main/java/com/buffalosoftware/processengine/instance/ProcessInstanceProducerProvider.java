package com.buffalosoftware.processengine.instance;

import com.buffalosoftware.api.processengine.IProcessInstanceProducerProvider;
import com.buffalosoftware.api.processengine.IProcessInstanceProducer;
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

    private static Map<ProcessType, IProcessInstanceProducer> processInstanceProducers = new HashMap<>();

    @Override
    public IProcessInstanceProducer getProducer(ProcessType processType) {
        return ofNullable(processInstanceProducers.get(processType)).orElseThrow(() -> new IllegalArgumentException("Process type is not supported!"));
    }

    @Autowired
    public void setProcessInstanceProducers(List<IProcessInstanceProducer> providers) {
        processInstanceProducers = providers.stream().collect(toMap(IProcessInstanceProducer::supportedProcessType, Function.identity()));
    }

}
