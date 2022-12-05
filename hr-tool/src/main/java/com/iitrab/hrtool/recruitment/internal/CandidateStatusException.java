package com.iitrab.hrtool.recruitment.internal;

import com.iitrab.hrtool.candidate.api.RecruitmentStatus;
import com.iitrab.hrtool.exception.api.BusinessException;

@SuppressWarnings("squid:S110")
class CandidateStatusException extends BusinessException {
    CandidateStatusException(String message) {
        super(message);
    }

    CandidateStatusException(Long id, RecruitmentStatus recruitmentStatus) {
        this("Candidate with ID=%s has status=%s".formatted(id,recruitmentStatus.toString()));
    }
}
