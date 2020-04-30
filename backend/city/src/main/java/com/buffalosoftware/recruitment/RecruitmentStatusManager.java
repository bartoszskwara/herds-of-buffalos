package com.buffalosoftware.recruitment;

import com.buffalosoftware.api.unit.IRecruitmentStatusManager;
import com.buffalosoftware.entity.Recruitment;
import com.buffalosoftware.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruitmentStatusManager implements IRecruitmentStatusManager {

    private final RecruitmentRepository recruitmentRepository;

    @Override
    public void startRecruitment(Long recruitmentId) {
        Optional<Recruitment> recruitment = recruitmentRepository.findById(recruitmentId);
        recruitment.ifPresent(r -> {
            r.start();
            recruitmentRepository.save(r);
        });
    }

    @Override
    public void completeRecruitment(Long recruitmentId) {
        Optional<Recruitment> recruitment = recruitmentRepository.findById(recruitmentId);
        recruitment.ifPresent(r -> {
            r.complete();
            recruitmentRepository.save(r);
        });
    }
}
