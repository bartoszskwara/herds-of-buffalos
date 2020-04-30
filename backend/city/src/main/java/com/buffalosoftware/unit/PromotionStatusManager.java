package com.buffalosoftware.unit;

import com.buffalosoftware.api.unit.IPromotionStatusManager;
import com.buffalosoftware.entity.Promotion;
import com.buffalosoftware.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionStatusManager implements IPromotionStatusManager {

    private final PromotionRepository promotionRepository;

    @Override
    public void startPromotion(Long promotionId) {
        Optional<Promotion> promotion = promotionRepository.findById(promotionId);
        promotion.ifPresent(r -> {
            r.start();
            promotionRepository.save(r);
        });
    }

    @Override
    public void completePromotion(Long promotionId) {
        Optional<Promotion> promotion = promotionRepository.findById(promotionId);
        promotion.ifPresent(r -> {
            r.complete();
            promotionRepository.save(r);
        });
    }
}
