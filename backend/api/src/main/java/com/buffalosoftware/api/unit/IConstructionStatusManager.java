package com.buffalosoftware.api.unit;

public interface IConstructionStatusManager {
    void startConstruction(Long constructionId);
    void completeConstruction(Long constructionId);
}
