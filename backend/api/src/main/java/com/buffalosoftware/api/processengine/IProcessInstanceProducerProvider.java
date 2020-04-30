package com.buffalosoftware.api.processengine;

public interface IProcessInstanceProducerProvider {
    AbstractProcessInstanceProducer getProducer(ProcessType processType);
}
