package com.buffalosoftware.api.unit;

public interface IRecruitmentStatusManager {
    void startRecruitment(Long recruitmentId);
    void completeRecruitment(Long recruitmentId);
}
