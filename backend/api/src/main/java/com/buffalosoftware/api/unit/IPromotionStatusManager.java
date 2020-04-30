package com.buffalosoftware.api.unit;

public interface IPromotionStatusManager {
    void startPromotion(Long recruitmentId);
    void completePromotion(Long recruitmentId);
}
