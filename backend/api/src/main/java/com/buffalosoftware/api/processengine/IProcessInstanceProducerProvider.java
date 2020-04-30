package com.buffalosoftware.api.processengine;

public interface IProcessInstanceProducerProvider {
    IProcessInstanceProducer getProducer(ProcessType processType);
}
