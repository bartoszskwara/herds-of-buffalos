package com.buffalosoftware.construction;

import com.buffalosoftware.api.unit.IConstructionStatusManager;
import com.buffalosoftware.entity.Construction;
import com.buffalosoftware.repository.ConstructionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConstructionStatusManager implements IConstructionStatusManager {

    private final ConstructionRepository constructionRepository;

    @Override
    public void startConstruction(Long constructionId) {
        Optional<Construction> construction = constructionRepository.findById(constructionId);
        construction.ifPresent(r -> {
            r.start();
            constructionRepository.save(r);
        });
    }

    @Override
    public void completeConstruction(Long constructionId) {
        Optional<Construction> construction = constructionRepository.findById(constructionId);
        construction.ifPresent(r -> {
            r.complete();
            constructionRepository.save(r);
        });
    }
}
